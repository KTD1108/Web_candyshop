package com.candyshop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.candyshop.dto.CategoryRequest;
import com.candyshop.entity.Category;
import com.candyshop.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//xử lý logic nghiệp vụ liên quan đến danh mục sản phẩm.
public class CategoryService {

	private final CategoryRepository categoryRepository;

	// Lấy tất cả các danh mục.
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	// Lấy một danh mục theo ID.
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
	}

	// Tạo một danh mục mới.
	public Category createCategory(CategoryRequest request) {
		Category category = new Category();
		category.setName(request.getName());
		category.setSlug(generateSlug(request.getName())); // Tạo slug từ tên danh mục
		return categoryRepository.save(category);
	}

	// Cập nhật một danh mục hiện có.
	public Category updateCategory(Long id, CategoryRequest request) {
		Category existingCategory = getCategoryById(id);
		existingCategory.setName(request.getName());
		existingCategory.setSlug(generateSlug(request.getName())); // Tạo lại slug khi tên thay đổi
		return categoryRepository.save(existingCategory);
	}

	// Xóa một danh mục.
	public void deleteCategory(Long id) {
		categoryRepository.deleteById(id);
	}

	// Tạo một slug từ tên và đảm bảo tính duy nhất.
	private String generateSlug(String name) {
		if (!StringUtils.hasText(name)) {
			return "";
		}
		// Chuyển đổi sang chữ thường, xóa khoảng trắng, thay thế ký tự không hợp lệ và
		// khoảng trắng.
		String slug = name.toLowerCase().trim().replaceAll("[^a-z0-9\\s-]", "") // Xóa các ký tự không hợp lệ
				.replaceAll("\\s+", "-") // Thay thế khoảng trắng bằng một dấu gạch ngang
				.replaceAll("-+", "-"); // Thay thế nhiều dấu gạch ngang bằng một

		// Đảm bảo slug là duy nhất bằng cách thêm số vào cuối nếu cần.
		String uniqueSlug = slug;
		int counter = 0;
		while (categoryRepository.findBySlug(uniqueSlug).isPresent()) {
			counter++;
			uniqueSlug = slug + "-" + counter;
		}
		return uniqueSlug;
	}
}
