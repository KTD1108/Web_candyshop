package com.candyshop.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vouchers", uniqueConstraints = { @UniqueConstraint(columnNames = { "code" }) })
@Getter
@Setter
public class Voucher {

	public enum DiscountType {
		PERCENTAGE, FIXED_AMOUNT
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String code;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DiscountType discountType;

	@Column(nullable = false)
	private BigDecimal discountValue;

	private BigDecimal maxDiscountAmount;

	@Column(nullable = false)
	private BigDecimal minOrderAmount = BigDecimal.ZERO;

	@Column(nullable = false)
	private Instant validFrom;

	@Column(nullable = false)
	private Instant validTo;

	@Column(nullable = false)
	private boolean isActive = true;

	@Column(nullable = false)
	private int usageLimit;

	@Column(nullable = false)
	private int currentUsage = 0;
}
