package com.candyshop.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateVoucherResponse {
    private String code;
    private BigDecimal discountAmount;
    private BigDecimal originalSubtotal;
    private BigDecimal finalSubtotal; // subtotal after discount
    private boolean success;
    private String message;
}
