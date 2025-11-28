package com.candyshop.controller;

import com.candyshop.entity.Category;
import com.candyshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin
// Lớp Controller xử lý các yêu cầu liên quan đến quản lý và truy xuất danh mục sản phẩm.
public class CategoryController {

    private final CategoryRepository categoryRepository;

    // Xử lý yêu cầu GET để lấy tất cả các danh mục sản phẩm.
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
