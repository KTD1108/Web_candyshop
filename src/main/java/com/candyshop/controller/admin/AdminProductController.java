package com.candyshop.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.dto.ProductRequest;
import com.candyshop.entity.Product;
import com.candyshop.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin
public class AdminProductController {

	private final ProductService productService;

	// Xử lý yêu cầu GET để lấy tất cả các sản phẩm.
	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	// Xử lý yêu cầu POST để tạo sản phẩm mới.
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
		Product createdProduct = productService.createProduct(productRequest);
		return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	}

	// Xử lý yêu cầu PUT để cập nhật thông tin sản phẩm hiện có dựa trên ID.
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
		Product updatedProduct = productService.updateProduct(id, productRequest);
		return ResponseEntity.ok(updatedProduct);
	}

	// Xử lý yêu cầu DELETE để xóa một sản phẩm dựa trên ID.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
