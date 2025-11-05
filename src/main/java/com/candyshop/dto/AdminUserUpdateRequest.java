package com.candyshop.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminUserUpdateRequest {
    private String email;
    private String fullName;
    private Boolean enabled;
    private List<Long> roleIds;
}
