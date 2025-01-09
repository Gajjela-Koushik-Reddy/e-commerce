package com.estore.product_service.dto;

import java.util.List;

public record ProductResponseDto(
        String Id,
        String productName,
        String price,
        String description,
        List<String> images) {
}
