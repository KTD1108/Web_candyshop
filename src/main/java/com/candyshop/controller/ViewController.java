package com.candyshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/product-detail.html")
    public String productDetail() {
        return "product-detail";
    }

    @GetMapping("/cart.html")
    public String cart() {
        return "cart";
    }

    @GetMapping("/checkout.html")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/orders.html")
    public String orders() {
        return "orders";
    }

    @GetMapping("/profile.html")
    public String profile() {
        return "profile";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact.html")
    public String contact() {
        return "contact";
    }

    @GetMapping("/admin-products.html")
    public String adminProducts() {
        return "admin-products";
    }

    @GetMapping("/admin-categories.html")
    public String adminCategories() {
        return "admin-categories";
    }

    @GetMapping("/admin-customers.html")
    public String adminCustomers() {
        return "admin-customers";
    }

    @GetMapping("/admin-orders.html")
    public String adminOrders() {
        return "admin-orders";
    }

    @GetMapping("/admin-statistics.html")
    public String adminStatistics() {
        return "admin-statistics";
    }

    @GetMapping("/admin-vouchers.html")
    public String adminVouchers() {
        return "admin-vouchers";
    }
}
