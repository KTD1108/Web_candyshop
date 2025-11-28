package com.candyshop.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candyshop.dto.CheckoutRequest;
import com.candyshop.entity.Cart;
import com.candyshop.entity.CartItem;
import com.candyshop.entity.Order;
import com.candyshop.entity.OrderItem;
import com.candyshop.entity.User;
import com.candyshop.entity.Voucher;
import com.candyshop.repository.CartItemRepository;
import com.candyshop.repository.CartRepository;
import com.candyshop.repository.OrderItemRepository;
import com.candyshop.repository.OrderRepository;
import com.candyshop.repository.VoucherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//  xử lý logic nghiệp vụ liên quan đến đơn hàng của người dùng, bao gồm quá trình thanh toán, truy xuất và cập nhật trạng thái đơn hàng.
public class OrderService {
	private final CartRepository cartRepo;
	private final CartItemRepository cartItemRepo;
	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final VoucherService voucherService;
	private final VoucherRepository voucherRepository;

	// Xử lý logic thanh toán, chuyển các mục từ giỏ hàng thành một đơn hàng mới.
	@Transactional
	public Order checkout(User user, CheckoutRequest dto) {
		// Tìm giỏ hàng của người dùng, nếu không có thì báo lỗi.
		Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
		List<CartItem> items = cart.getCartItems();
		if (items.isEmpty()) {
			throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng");
		}

		// Bước 1: Tính tổng tiền hàng (subtotal) từ các sản phẩm trong giỏ.
		BigDecimal subtotal = items.stream()
				.map(ci -> ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// Bước 2: Tính phí vận chuyển dựa trên phương thức vận chuyển.
		BigDecimal shippingFee = "EXPRESS".equalsIgnoreCase(dto.getShippingMethod()) ? new BigDecimal("40000")
				: new BigDecimal("20000");

		// Bước 3: Áp dụng mã giảm giá (voucher) nếu có.
		BigDecimal discountAmount = BigDecimal.ZERO;
		Voucher voucher = null;
		if (dto.getVoucherCode() != null && !dto.getVoucherCode().isBlank()) {
			voucher = voucherService.findByCode(dto.getVoucherCode());
			discountAmount = voucherService.calculateDiscount(voucher, subtotal);
		}

		// Bước 4: Tính tổng số tiền cuối cùng sau khi áp dụng phí vận chuyển và giảm
		// giá.
		BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);
		if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
			totalAmount = BigDecimal.ZERO; // Đảm bảo tổng tiền không âm.
		}

		// Bước 5: Tạo đối tượng đơn hàng mới và điền thông tin.
		Order order = new Order();
		order.setUser(user);
		order.setTotalAmount(totalAmount);
		order.setShippingFee(shippingFee);
		order.setShippingMethod(dto.getShippingMethod());
		order.setPaymentMethod(dto.getPaymentMethod());
		order.setOrderNotes(dto.getOrderNotes());
		order.setStatus("PENDING"); // Đặt trạng thái ban đầu là PENDING.
		order.setDiscountAmount(discountAmount);
		order.setVoucherCode(dto.getVoucherCode());

		// Bước 6: Thiết lập địa chỉ và số điện thoại giao hàng.
		if (dto.isUseProfileAddress()) {
			// Sử dụng địa chỉ và số điện thoại từ hồ sơ người dùng.
			if (user.getAddress() == null || user.getAddress().isBlank() || user.getPhone() == null
					|| user.getPhone().isBlank()) {
				throw new RuntimeException("Vui lòng cập nhật địa chỉ và số điện thoại trong hồ sơ của bạn trước.");
			}
			order.setShippingAddress(user.getAddress());
			order.setShippingPhone(user.getPhone());
		} else {
			// Sử dụng địa chỉ và số điện thoại cung cấp trong yêu cầu thanh toán.
			if (dto.getAddress() == null || dto.getAddress().isBlank() || dto.getCustomerPhone() == null
					|| dto.getCustomerPhone().isBlank()) {
				throw new RuntimeException("Vui lòng cung cấp địa chỉ và số điện thoại giao hàng.");
			}
			order.setShippingAddress(dto.getAddress());
			order.setShippingPhone(dto.getCustomerPhone());
		}

		// Lưu đơn hàng vào cơ sở dữ liệu.
		order = orderRepo.save(order);

		// Bước 7: Chuyển các CartItem thành OrderItem và lưu vào cơ sở dữ liệu.
		for (CartItem ci : items) {
			OrderItem oi = new OrderItem();
			oi.setOrder(order);
			oi.setProduct(ci.getProduct());
			oi.setPrice(ci.getProduct().getPrice());
			oi.setQuantity(ci.getQuantity());
			orderItemRepo.save(oi);
		}

		// Bước 8: Cập nhật số lần sử dụng của mã giảm giá.
		if (voucher != null) {
			voucher.setCurrentUsage(voucher.getCurrentUsage() + 1);
			voucherRepository.save(voucher);
		}

		// Bước 9: Xóa các mục trong giỏ hàng sau khi đã đặt hàng thành công.
		cartItemRepo.deleteAllInBatch(items);

		return order;
	}

	// Lấy danh sách các đơn hàng của một người dùng cụ thể.
	public List<Order> myOrders(User user) {
		return orderRepo.findByUserOrderByCreatedAtAsc(user);
	}

	// Lấy tất cả các đơn hàng
	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}

	// Lấy thông tin chi tiết một đơn hàng bằng ID.
	public Order getOrderById(Long id) {
		return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
	}

	// Cập nhật trạng thái của một đơn hàng.
	@Transactional
	public Order updateOrderStatus(Long id, String newStatus) {
		Order order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus(newStatus);
		return orderRepo.save(order);
	}

	// Xóa một đơn hàng bằng ID
	public void deleteOrder(Long id) {
		orderRepo.deleteById(id);
	}

}
