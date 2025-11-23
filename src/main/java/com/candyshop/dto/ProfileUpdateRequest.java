package com.candyshop.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String fullName;
    private String address;
    private String phone;
}
