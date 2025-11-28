package com.candyshop.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.candyshop.entity.Voucher;
import com.candyshop.entity.Voucher.DiscountType;
import com.candyshop.repository.VoucherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//xử lý logic nghiệp vụ liên quan đến quản lý mã giảm giá (voucher), bao gồm xác thực, tính toán giảm giá và các thao tác CRUD.
public class VoucherService {

	private final VoucherRepository voucherRepository;

	public Voucher findByCode(String code) {
		return voucherRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Mã giảm giá không hợp lệ"));
	}

	// Tính toán số tiền giảm giá dựa trên mã giảm giá và tổng tiền đơn hàng phụ
	// (subtotal).
	public BigDecimal calculateDiscount(Voucher voucher, BigDecimal subtotal) {
		// ----- Quy tắc xác thực mã giảm giá -----
		if (!voucher.isActive()) {
			throw new RuntimeException("Mã giảm giá đã bị vô hiệu hóa.");
		}
		if (Instant.now().isBefore(voucher.getValidFrom())) {
			throw new RuntimeException("Mã giảm giá chưa có hiệu lực.");
		}
		if (Instant.now().isAfter(voucher.getValidTo())) {
			throw new RuntimeException("Mã giảm giá đã hết hạn.");
		}
		if (voucher.getCurrentUsage() >= voucher.getUsageLimit()) {
			throw new RuntimeException("Mã giảm giá đã hết lượt sử dụng.");
		}
		if (subtotal.compareTo(voucher.getMinOrderAmount()) < 0) {
			throw new RuntimeException(String.format("Đơn hàng tối thiểu phải là %s đ để áp dụng mã này.",
					voucher.getMinOrderAmount().toPlainString()));
		}

		// ----- Tính toán giảm giá -----
		BigDecimal discountAmount;
		if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
			// Tính giảm giá theo phần trăm.
			discountAmount = subtotal.multiply(voucher.getDiscountValue().divide(new BigDecimal("100")));
			// Áp dụng giới hạn giảm giá tối đa nếu có.
			if (voucher.getMaxDiscountAmount() != null
					&& discountAmount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
				discountAmount = voucher.getMaxDiscountAmount();
			}
		} else { // FIXED_AMOUNT
			// Giảm giá theo số tiền cố định.
			discountAmount = voucher.getDiscountValue();
		}

		return discountAmount;
	}

	// Lấy danh sách các mã giảm giá hiện có thể áp dụng dựa trên tổng tiền đơn hàng
	// phụ.
	public List<Voucher> getAvailableVouchers(BigDecimal subtotal) {
		List<Voucher> allVouchers = voucherRepository.findAll();
		Instant now = Instant.now();

		// Lọc các mã giảm giá theo điều kiện: đang hoạt động, chưa hết hạn, đã có hiệu
		// lực,
		// còn lượt sử dụng và đáp ứng tổng tiền đơn hàng tối thiểu.
		return allVouchers.stream().filter(Voucher::isActive).filter(v -> !now.isAfter(v.getValidTo()))
				.filter(v -> !now.isBefore(v.getValidFrom())).filter(v -> v.getCurrentUsage() < v.getUsageLimit())
				.filter(v -> subtotal.compareTo(v.getMinOrderAmount()) >= 0).toList();
	}

	// ----- Các thao tác CRUD cho quản trị viên -----

	// Lấy tất cả các mã giảm giá.
	public List<Voucher> findAll() {
		return voucherRepository.findAll();
	}

	// Tìm mã giảm giá bằng ID.
	public Optional<Voucher> findById(Long id) {
		return voucherRepository.findById(id);
	}

	// Lưu (tạo mới hoặc cập nhật) một mã giảm giá.
	public Voucher save(Voucher voucher) {
		return voucherRepository.save(voucher);
	}

	// Xóa một mã giảm giá bằng ID.
	public void deleteById(Long id) {
		voucherRepository.deleteById(id);
	}
}
