package com.estore.product_service.dto;

public record ProductDto(
        String catrgoryId,
        String productName,
        String price,
        String description,
        String shortDescription,
        Integer stockQuantity) {
}
