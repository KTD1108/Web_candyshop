package com.candyshop.controller.admin;

import java.util.List;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candyshop.entity.Voucher;
import com.candyshop.service.VoucherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/vouchers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminVoucherController {

	private final VoucherService voucherService;

	@GetMapping
	public ResponseEntity<List<Voucher>> getAllVouchers() {
		return ResponseEntity.ok(voucherService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Voucher> getVoucherById(@PathVariable Long id) {
		return voucherService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
		// Ensure ID is null for creation
		voucher.setId(null);
		Voucher createdVoucher = voucherService.save(voucher);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdVoucher);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Voucher> updateVoucher(@PathVariable Long id, @RequestBody Voucher voucher) {
		return voucherService.findById(id).map(existingVoucher -> {
			existingVoucher.setCode(voucher.getCode());
			existingVoucher.setDiscountType(voucher.getDiscountType()); // Added this line
			existingVoucher.setDiscountValue(voucher.getDiscountValue());
			existingVoucher.setMinOrderAmount(voucher.getMinOrderAmount());
			existingVoucher.setUsageLimit(voucher.getUsageLimit());
			existingVoucher.setValidTo(voucher.getValidTo());
			existingVoucher.setActive(voucher.isActive());
			// existingVoucher.setCurrentUsage(voucher.getCurrentUsage()); // Used count should not be updated directly by admin
			return ResponseEntity.ok(voucherService.save(existingVoucher));
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
		if (voucherService.findById(id).isPresent()) {
			voucherService.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
