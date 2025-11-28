package com.candyshop.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.dto.OrderStatusUpdateRequest;
import com.candyshop.entity.Order;
import com.candyshop.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin
public class AdminOrderController {

	private final OrderService orderService;

	// Xử lý yêu cầu GET để lấy tất cả các đơn hàng.
	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	// Xử lý yêu cầu GET để lấy thông tin một đơn hàng cụ thể dựa trên ID.
	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	// Xử lý yêu cầu PUT để cập nhật trạng thái của một đơn hàng.
	@PutMapping("/{id}/status")
	public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id,
			@RequestBody OrderStatusUpdateRequest request) {
		Order updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
		return ResponseEntity.ok(updatedOrder);
	}

	// Xử lý yêu cầu DELETE để xóa một đơn hàng.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.noContent().build();
	}
}
