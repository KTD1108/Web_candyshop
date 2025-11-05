package com.candyshop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.candyshop.dto.ProductRequest;
import com.candyshop.entity.Category;
import com.candyshop.entity.Product;
import com.candyshop.repository.CategoryRepository;
import com.candyshop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Page<Product> search(String kw, Long categoryId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
		if (categoryId != null) {
			if (kw != null && !kw.isBlank()) {
				return productRepository.findAllByNameContainingAndCategoryId(kw, categoryId, pageable);
			}
			return productRepository.findAllByCategoryId(categoryId, pageable);
		}
		if (kw != null && !kw.isBlank()) {
			return productRepository.findAllByNameContaining(kw, pageable);
		}
		return productRepository.findAll(pageable);
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	}

	public Product createProduct(ProductRequest productRequest) {
		Product product = new Product();
		product.setName(productRequest.getName());
		product.setDescription(productRequest.getDescription());
		product.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
		product.setThumbnailUrl(productRequest.getImageUrl());
		product.setStockQty(productRequest.getStockQty() != null ? productRequest.getStockQty() : 0); // Set stock quantity, default to 0

		// Generate and set slug
		String baseSlug = generateSlug(productRequest.getName());
		product.setSlug(generateUniqueSlug(baseSlug, null)); // null for create

		Category category = categoryRepository.findById(productRequest.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		product.setCategory(category);

		return productRepository.save(product);
	}

	public Product updateProduct(Long id, ProductRequest productRequest) {
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		if (!existingProduct.getName().equals(productRequest.getName())) {
			// If name changes, regenerate slug
			String baseSlug = generateSlug(productRequest.getName());
			existingProduct.setSlug(generateUniqueSlug(baseSlug, id)); // pass id for update
		}

		existingProduct.setName(productRequest.getName());
		existingProduct.setDescription(productRequest.getDescription());
		existingProduct.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
		existingProduct.setThumbnailUrl(productRequest.getImageUrl());
		if (productRequest.getStockQty() != null) {
			existingProduct.setStockQty(productRequest.getStockQty());
		}

		Category category = categoryRepository.findById(productRequest.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		existingProduct.setCategory(category);

		return productRepository.save(existingProduct);
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

	// Helper method to generate a slug from a product name
	private String generateSlug(String name) {
		if (!StringUtils.hasText(name)) {
			return "";
		}
		String slug = name.toLowerCase().trim()
				.replaceAll("[^a-z0-9\\s-]", "") // Remove invalid chars
				.replaceAll("\\s+", "-") // Replace spaces with a single hyphen
				.replaceAll("-+", "-"); // Replace multiple hyphens with a single hyphen
		return slug;
	}

	// Helper method to ensure slug uniqueness
	private String generateUniqueSlug(String baseSlug, Long productId) {
		String uniqueSlug = baseSlug;
		int counter = 0;
		while (productRepository.findBySlug(uniqueSlug).isPresent()) {
			Optional<Product> existingProductOptional = productRepository.findBySlug(uniqueSlug);
			if (existingProductOptional.isPresent() && !existingProductOptional.get().getId().equals(productId)) {
				counter++;
				uniqueSlug = baseSlug + "-" + counter;
			} else {
				break; // Slug is unique for this product or the product itself
			}
		}
		return uniqueSlug;
	}
}
