package com.candyshop.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequest {
    private String email;
    private String fullName;
    private Boolean enabled;
    private Set<Long> roleIds;
}
