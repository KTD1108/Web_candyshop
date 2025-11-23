package com.candyshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.dto.ValidateVoucherRequest;
import com.candyshop.dto.ValidateVoucherResponse;
import com.candyshop.entity.Voucher;
import com.candyshop.service.VoucherService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
@CrossOrigin
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/validate")
    public ResponseEntity<ValidateVoucherResponse> validateVoucher(@RequestBody ValidateVoucherRequest request) {
        try {
            Voucher voucher = voucherService.findByCode(request.getCode());
            BigDecimal discountAmount = voucherService.calculateDiscount(voucher, request.getSubtotal());
            
            ValidateVoucherResponse response = ValidateVoucherResponse.builder()
                .success(true)
                .message("Áp dụng mã giảm giá thành công!")
                .code(voucher.getCode())
                .discountAmount(discountAmount)
                .originalSubtotal(request.getSubtotal())
                .finalSubtotal(request.getSubtotal().subtract(discountAmount))
                .build();
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ValidateVoucherResponse response = ValidateVoucherResponse.builder()
                .success(false)
                .message(e.getMessage())
                .code(request.getCode())
                .originalSubtotal(request.getSubtotal())
                .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Voucher>> getAvailableVouchers(@RequestParam String subtotal) {
        BigDecimal subtotalDecimal = new BigDecimal(subtotal);
        return ResponseEntity.ok(voucherService.getAvailableVouchers(subtotalDecimal));
    }
}
