package com.candyshop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candyshop.dto.CheckoutRequest;
import com.candyshop.entity.Cart;
import com.candyshop.entity.CartItem;
import com.candyshop.entity.Order;
import com.candyshop.entity.OrderItem;
import com.candyshop.entity.User;
import com.candyshop.repository.CartItemRepository;
import com.candyshop.repository.CartRepository;
import com.candyshop.repository.OrderItemRepository;
import com.candyshop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final CartRepository cartRepo;
	private final CartItemRepository cartItemRepo;
	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;

	@Transactional
	public Order checkout(User user, CheckoutRequest dto) {
		Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
		List<CartItem> items = cart.getCartItems();
		if (items.isEmpty()) {
			throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng");
		}

		BigDecimal total = items.stream()
				.map(ci -> ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		String shippingAddress = String.format("%s, %s, %s", dto.getCustomerName(), dto.getCustomerPhone(),
				dto.getAddress());

		Order order = new Order();
		order.setUser(user);
		order.setTotalAmount(total);
		order.setStatus("PENDING");
		order.setShippingAddress(shippingAddress);
		order = orderRepo.save(order);

		for (CartItem ci : items) {
			OrderItem oi = new OrderItem();
			oi.setOrder(order);
			oi.setProduct(ci.getProduct());
			oi.setPrice(ci.getProduct().getPrice());
			oi.setQuantity(ci.getQuantity());
			orderItemRepo.save(oi);
		}

		// Clear the cart by deleting all items
		cartItemRepo.deleteAllInBatch(items);

		return order;
	}

	public List<Order> myOrders(User user) {
		return orderRepo.findByUserOrderByCreatedAtAsc(user);
	}

	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}

	public Order getOrderById(Long id) {
		return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
	}

	@Transactional
	public Order updateOrderStatus(Long id, String newStatus) {
		Order order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus(newStatus);
		return orderRepo.save(order);
	}

	public void deleteOrder(Long id) {
		orderRepo.deleteById(id);
	}

	// Placeholder implementations for StatisticsController
	public BigDecimal getRevenueToday() {
		return BigDecimal.ZERO; // Implement actual logic to fetch today's revenue
	}

	public BigDecimal getRevenueThisMonth() {
		return BigDecimal.ZERO; // Implement actual logic to fetch this month's revenue
	}

	public BigDecimal getRevenueThisYear() {
		return BigDecimal.ZERO; // Implement actual logic to fetch this year's revenue
	}

	public List<Map<String, Object>> getMonthlyRevenue() {
		return List.of(); // Implement actual logic to fetch monthly revenue
	}

	public List<Map<String, Object>> getBestSellingProducts(int limit) {
		return List.of(); // Implement actual logic to fetch best-selling products
	}
}
