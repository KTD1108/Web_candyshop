package com.candyshop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.dto.CheckoutRequest;
import com.candyshop.entity.Order;
import com.candyshop.security.CustomUserDetails;
import com.candyshop.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

	private final OrderService orderService;

	/** Đặt hàng từ giỏ */
	@PostMapping
	public ResponseEntity<?> checkout(
			@org.springframework.security.core.annotation.AuthenticationPrincipal CustomUserDetails cud,
			@Valid @RequestBody CheckoutRequest dto) {
		Order order = orderService.checkout(cud.getUser(), dto);
		return ResponseEntity.ok(Map.of("orderId", order.getId(), "status", order.getStatus()));
	}

	/** Lấy danh sách đơn hàng của user hiện tại */
	@GetMapping
	public List<Order> myOrders(
			@org.springframework.security.core.annotation.AuthenticationPrincipal CustomUserDetails cud) {
		return orderService.myOrders(cud.getUser());
	}
}
