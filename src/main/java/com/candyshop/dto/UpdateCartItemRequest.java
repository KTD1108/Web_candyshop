package com.candyshop.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private int quantity;
}
