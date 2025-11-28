package com.candyshop.init;

import com.candyshop.entity.Voucher;
import com.candyshop.entity.Voucher.DiscountType;
import com.candyshop.repository.VoucherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
// Lớp này chịu trách nhiệm tải dữ liệu mã giảm giá khởi tạo vào cơ sở dữ liệu khi ứng dụng bắt đầu chạy.
public class VoucherDataLoader implements CommandLineRunner {

    private final VoucherRepository voucherRepository;

    public VoucherDataLoader(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    // Phương thức này được thực thi tự động khi ứng dụng Spring Boot khởi động.
    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra xem đã có mã giảm giá nào trong cơ sở dữ liệu chưa để tránh tạo trùng lặp.
        if (voucherRepository.count() == 0) {
            // Tạo và lưu mã giảm giá cố định (FIXED_AMOUNT).
            Voucher voucher1 = new Voucher();
            voucher1.setCode("GIAM10K");
            voucher1.setDiscountType(DiscountType.FIXED_AMOUNT);
            voucher1.setDiscountValue(new BigDecimal("10000"));
            voucher1.setMinOrderAmount(new BigDecimal("50000"));
            voucher1.setValidFrom(Instant.now());
            voucher1.setValidTo(Instant.now().plus(30, ChronoUnit.DAYS));
            voucher1.setUsageLimit(100);
            voucherRepository.save(voucher1);

            // Tạo và lưu mã giảm giá theo phần trăm (PERCENTAGE).
            Voucher voucher2 = new Voucher();
            voucher2.setCode("SALE10");
            voucher2.setDiscountType(DiscountType.PERCENTAGE);
            voucher2.setDiscountValue(new BigDecimal("10")); // 10%
            voucher2.setMaxDiscountAmount(new BigDecimal("25000"));
            voucher2.setMinOrderAmount(new BigDecimal("100000"));
            voucher2.setValidFrom(Instant.now());
            voucher2.setValidTo(Instant.now().plus(60, ChronoUnit.DAYS));
            voucher2.setUsageLimit(200);
            voucherRepository.save(voucher2);
            
            // Tạo và lưu một mã giảm giá đã hết hạn để kiểm thử.
            Voucher expiredVoucher = new Voucher();
            expiredVoucher.setCode("HETHAN");
            expiredVoucher.setDiscountType(DiscountType.FIXED_AMOUNT);
            expiredVoucher.setDiscountValue(new BigDecimal("100000"));
            expiredVoucher.setMinOrderAmount(new BigDecimal("50000"));
            expiredVoucher.setValidFrom(Instant.now().minus(60, ChronoUnit.DAYS));
            expiredVoucher.setValidTo(Instant.now().minus(30, ChronoUnit.DAYS));
            expiredVoucher.setUsageLimit(100);
            voucherRepository.save(expiredVoucher);
        }
    }
}
