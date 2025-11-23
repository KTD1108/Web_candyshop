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

import com.candyshop.entity.Voucher;
import com.candyshop.repository.VoucherRepository;
import com.candyshop.service.VoucherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final CartRepository cartRepo;
	private final CartItemRepository cartItemRepo;
	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final VoucherService voucherService;
	private final VoucherRepository voucherRepository;

	@Transactional
	public Order checkout(User user, CheckoutRequest dto) {
		Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
		List<CartItem> items = cart.getCartItems();
		if (items.isEmpty()) {
			throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng");
		}

		BigDecimal subtotal = items.stream()
				.map(ci -> ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal shippingFee = "EXPRESS".equalsIgnoreCase(dto.getShippingMethod())
				? new BigDecimal("40000")
				: new BigDecimal("20000");

		BigDecimal discountAmount = BigDecimal.ZERO;
		Voucher voucher = null;
		if (dto.getVoucherCode() != null && !dto.getVoucherCode().isBlank()) {
			voucher = voucherService.findByCode(dto.getVoucherCode());
			discountAmount = voucherService.calculateDiscount(voucher, subtotal);
		}

		BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);
		if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
			totalAmount = BigDecimal.ZERO;
		}

		Order order = new Order();
		order.setUser(user);
		order.setTotalAmount(totalAmount);
		order.setShippingFee(shippingFee);
		order.setShippingMethod(dto.getShippingMethod());
		order.setPaymentMethod(dto.getPaymentMethod());
		order.setOrderNotes(dto.getOrderNotes());
		order.setStatus("PENDING");
		order.setDiscountAmount(discountAmount);
		order.setVoucherCode(dto.getVoucherCode());

		if (dto.isUseProfileAddress()) {
			if (user.getAddress() == null || user.getAddress().isBlank() || user.getPhone() == null || user.getPhone().isBlank()) {
				throw new RuntimeException("Vui lòng cập nhật địa chỉ và số điện thoại trong hồ sơ của bạn trước.");
			}
			order.setShippingAddress(user.getAddress());
			order.setShippingPhone(user.getPhone());
		} else {
			if (dto.getAddress() == null || dto.getAddress().isBlank() || dto.getCustomerPhone() == null || dto.getCustomerPhone().isBlank()) {
				throw new RuntimeException("Vui lòng cung cấp địa chỉ và số điện thoại giao hàng.");
			}
			order.setShippingAddress(dto.getAddress());
			order.setShippingPhone(dto.getCustomerPhone());
		}

		order = orderRepo.save(order);

		for (CartItem ci : items) {
			OrderItem oi = new OrderItem();
			oi.setOrder(order);
			oi.setProduct(ci.getProduct());
			oi.setPrice(ci.getProduct().getPrice());
			oi.setQuantity(ci.getQuantity());
			orderItemRepo.save(oi);
		}

		if (voucher != null) {
			voucher.setCurrentUsage(voucher.getCurrentUsage() + 1);
			voucherRepository.save(voucher);
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
