package com.candyshop.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.entity.Product;
import com.candyshop.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin
// Lớp Controller xử lý các yêu cầu liên quan đến hiển thị và tìm kiếm sản phẩm.
public class ProductController {

	private final ProductService service;

	// Xử lý yêu cầu GET để lấy danh sách sản phẩm với các tùy chọn tìm kiếm, phân
	// trang và sắp xếp.
	@GetMapping
	public Page<Product> list(@RequestParam(required = false) String kw,
			@RequestParam(required = false) Long categoryId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int size, @RequestParam(required = false) String sort,
			@RequestParam(required = false) String direction) {
		return service.search(kw, categoryId, page, size, sort, direction);
	}

	// Xử lý yêu cầu GET để lấy thông tin chi tiết của một sản phẩm dựa trên ID.
	@GetMapping("/{id}")
	public Product detail(@PathVariable Long id) {
		return service.getProductById(id);
	}
}
