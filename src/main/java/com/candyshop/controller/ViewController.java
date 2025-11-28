package com.candyshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// Lớp Controller này chịu trách nhiệm điều hướng và trả về các trang HTML (views) cho người dùng.
public class ViewController {

	// Trả về trang chủ (index.html).
	@GetMapping("/")
	public String index() {
		return "index";
	}

	// Trả về trang đăng nhập (login.html).
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// Trả về trang đăng ký (register.html).
	@GetMapping("/register")
	public String register() {
		return "register";
	}

	// Trả về trang chi tiết sản phẩm (product-detail.html).
	@GetMapping("/product-detail.html")
	public String productDetail() {
		return "product-detail";
	}

	// Trả về trang giỏ hàng (cart.html).
	@GetMapping("/cart.html")
	public String cart() {
		return "cart";
	}

	// Trả về trang thanh toán (checkout.html).
	@GetMapping("/checkout.html")
	public String checkout() {
		return "checkout";
	}

	// Trả về trang đơn hàng của tôi (orders.html).
	@GetMapping("/orders.html")
	public String orders() {
		return "orders";
	}

	// Trả về trang hồ sơ người dùng (profile.html).
	@GetMapping("/profile.html")
	public String profile() {
		return "profile";
	}

	// Trả về trang giới thiệu (about.html).
	@GetMapping("/about")
	public String about() {
		return "about";
	}

	// Trả về trang quản lý sản phẩm cho admin (admin-products.html).
	@GetMapping("/admin-products.html")
	public String adminProducts() {
		return "admin-products";
	}

	// Trả về trang quản lý khách hàng cho admin (admin-customers.html).
	@GetMapping("/admin-customers.html")
	public String adminCustomers() {
		return "admin-customers";
	}

	// Trả về trang quản lý đơn hàng cho admin (admin-orders.html).
	@GetMapping("/admin-orders.html")
	public String adminOrders() {
		return "admin-orders";
	}

	// Trả về trang quản lý mã giảm giá cho admin (admin-vouchers.html).
	@GetMapping("/admin-vouchers.html")
	public String adminVouchers() {
		return "admin-vouchers";
	}
}
