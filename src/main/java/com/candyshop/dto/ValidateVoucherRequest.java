package com.candyshop.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ValidateVoucherRequest {
    private String code;
    private BigDecimal subtotal;
}
