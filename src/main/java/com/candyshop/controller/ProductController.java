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
public class ProductController {

    private final ProductService service;

    /** Danh sách sản phẩm (có tìm kiếm kw) */
    @GetMapping
    public Page<Product> list(@RequestParam(required=false) String kw,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(defaultValue="0") int page,
                              @RequestParam(defaultValue="12") int size){
        return service.search(kw, categoryId, page, size);
    }

    /** Chi tiết sản phẩm */
    @GetMapping("/{id}")
    public Product detail(@PathVariable Long id){
        return service.getProductById(id);
    }
}
