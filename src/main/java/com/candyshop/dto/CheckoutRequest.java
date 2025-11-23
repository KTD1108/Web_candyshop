package com.candyshop.dto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private boolean useProfileAddress;
    private String customerName;
    private String customerPhone;
    private String address;
    private String paymentMethod;
    private String shippingMethod;
    private String orderNotes;
    private String voucherCode;
}
