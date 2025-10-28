package com.candyshop.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private Long categoryId;
    private String imageUrl;
}
