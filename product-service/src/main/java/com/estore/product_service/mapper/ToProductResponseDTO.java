package com.estore.product_service.mapper;

import java.util.List;

public record ToProductResponseDTO(
        String Id,
        String productName,
        String price,
        String description,
        List<String> images) {
}
