package com.candyshop.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.candyshop.entity.Voucher;
import com.candyshop.entity.Voucher.DiscountType;
import com.candyshop.repository.VoucherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoucherService {

	private final VoucherRepository voucherRepository;

	public Voucher findByCode(String code) {
		return voucherRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Mã giảm giá không hợp lệ"));
	}

	public BigDecimal calculateDiscount(Voucher voucher, BigDecimal subtotal) {
		// Rule validation
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

		// Discount calculation
		BigDecimal discountAmount;
		if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
			discountAmount = subtotal.multiply(voucher.getDiscountValue().divide(new BigDecimal("100")));
			if (voucher.getMaxDiscountAmount() != null
					&& discountAmount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
				discountAmount = voucher.getMaxDiscountAmount();
			}
		} else { // FIXED_AMOUNT
			discountAmount = voucher.getDiscountValue();
		}

		return discountAmount;
	}

	public List<Voucher> getAvailableVouchers(BigDecimal subtotal) {
		List<Voucher> allVouchers = voucherRepository.findAll();
		Instant now = Instant.now();

		return allVouchers.stream().filter(Voucher::isActive).filter(v -> !now.isAfter(v.getValidTo()))
				.filter(v -> !now.isBefore(v.getValidFrom())).filter(v -> v.getCurrentUsage() < v.getUsageLimit())
				.filter(v -> subtotal.compareTo(v.getMinOrderAmount()) >= 0).toList();
	}
}
