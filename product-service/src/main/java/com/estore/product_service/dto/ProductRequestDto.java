package com.estore.product_service.dto;

import java.util.List;

public record ProductRequestDto(
        String Id,
        String catrgoryId,
        String productName,
        String price,
        String description,
        String shortDescription,
        Integer stockQuantity,
        List<String> images) {
}
