package com.estore.order_service.dto;

public record OrderRequest(
        Long id, Integer orderNumber, String skuCode, Long price, Integer quantity) {

}
