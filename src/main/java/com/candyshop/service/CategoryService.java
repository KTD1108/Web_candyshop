package com.candyshop.service;

import com.candyshop.dto.CategoryRequest;
import com.candyshop.entity.Category;
import com.candyshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(generateSlug(request.getName()));
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequest request) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(request.getName());
        existingCategory.setSlug(generateSlug(request.getName()));
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private String generateSlug(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }
        String slug = name.toLowerCase().trim()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove invalid chars
                .replaceAll("\\s+", "-") // Replace spaces with a single hyphen
                .replaceAll("-+", "-"); // Replace multiple hyphens with a single hyphen

        // Ensure slug uniqueness
        String uniqueSlug = slug;
        int counter = 0;
        while (categoryRepository.findBySlug(uniqueSlug).isPresent()) {
            counter++;
            uniqueSlug = slug + "-" + counter;
        }
        return uniqueSlug;
    }
}
