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
// xử lý logic nghiệp vụ liên quan đến quản lý sản phẩm.
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	// Lấy tất cả các sản phẩm.
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// Tìm kiếm và lọc sản phẩm với các tùy chọn phân trang và sắp xếp.
	public Page<Product> search(String kw, Long categoryId, int page, int size, String sort, String direction) {
		Sort sortOrder;
		// Xác định thứ tự sắp xếp theo giá hoặc theo tên.
		if ("price".equals(sort)) {
			Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC
					: Sort.Direction.ASC;
			sortOrder = Sort.by(sortDirection, "price");
		} else {
			// Mặc định sắp xếp theo tên.
			sortOrder = Sort.by("name");
		}

		Pageable pageable = PageRequest.of(page, size, sortOrder);

		// Logic tìm kiếm dựa trên categoryId và/hoặc từ khóa (kw).
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

	// Lấy thông tin chi tiết một sản phẩm bằng ID.
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	}

	// Tạo một sản phẩm mới.
	public Product createProduct(ProductRequest productRequest) {
		Product product = new Product();
		product.setName(productRequest.getName());
		product.setDescription(productRequest.getDescription());
		product.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
		product.setThumbnailUrl(productRequest.getImageUrl());
		product.setStockQty(productRequest.getStockQty() != null ? productRequest.getStockQty() : 0); // Đặt số lượng
																										// tồn kho, mặc
																										// định là 0

		// Tạo và đặt slug cho sản phẩm.
		String baseSlug = generateSlug(productRequest.getName());
		product.setSlug(generateUniqueSlug(baseSlug, null)); // null cho hành động tạo mới

		// Tìm và gán danh mục cho sản phẩm.
		Category category = categoryRepository.findById(productRequest.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		product.setCategory(category);

		return productRepository.save(product);
	}

	// Cập nhật thông tin một sản phẩm hiện có.
	public Product updateProduct(Long id, ProductRequest productRequest) {
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		// Nếu tên sản phẩm thay đổi, tạo lại slug mới và đảm bảo tính duy nhất.
		if (!existingProduct.getName().equals(productRequest.getName())) {
			String baseSlug = generateSlug(productRequest.getName());
			existingProduct.setSlug(generateUniqueSlug(baseSlug, id)); // truyền id cho hành động cập nhật
		}

		existingProduct.setName(productRequest.getName());
		existingProduct.setDescription(productRequest.getDescription());
		existingProduct.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
		existingProduct.setThumbnailUrl(productRequest.getImageUrl());
		if (productRequest.getStockQty() != null) {
			existingProduct.setStockQty(productRequest.getStockQty());
		}

		// Tìm và cập nhật danh mục sản phẩm.
		Category category = categoryRepository.findById(productRequest.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		existingProduct.setCategory(category);

		return productRepository.save(existingProduct);
	}

	// Xóa một sản phẩm bằng ID.
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

	// Phương thức trợ giúp để tạo slug từ tên sản phẩm.
	private String generateSlug(String name) {
		if (!StringUtils.hasText(name)) {
			return "";
		}
		// Chuyển đổi tên thành slug
		String slug = name.toLowerCase().trim().replaceAll("[^a-z0-9\\s-]", "") // Xóa các ký tự không hợp lệ
				.replaceAll("\\s+", "-") // Thay thế khoảng trắng bằng dấu gạch ngang
				.replaceAll("-+", "-"); // Thay thế nhiều dấu gạch ngang bằng một
		return slug;
	}

	// Phương thức trợ giúp để đảm bảo slug là duy nhất.
	private String generateUniqueSlug(String baseSlug, Long productId) {
		String uniqueSlug = baseSlug;
		int counter = 0;
		while (productRepository.findBySlug(uniqueSlug).isPresent()) {
			Optional<Product> existingProductOptional = productRepository.findBySlug(uniqueSlug);
			// Nếu slug tìm thấy thuộc về một sản phẩm khác (không phải sản phẩm đang được
			// cập nhật),
			// hoặc nếu đang tạo mới và slug đã tồn tại, thì tạo slug mới với hậu tố số.
			if (existingProductOptional.isPresent()
					&& (productId == null || !existingProductOptional.get().getId().equals(productId))) {
				counter++;
				uniqueSlug = baseSlug + "-" + counter;
			} else {
				break; // Slug là duy nhất cho sản phẩm này hoặc slug tìm thấy là của chính sản phẩm
						// đang cập nhật.
			}
		}
		return uniqueSlug;
	}
}
