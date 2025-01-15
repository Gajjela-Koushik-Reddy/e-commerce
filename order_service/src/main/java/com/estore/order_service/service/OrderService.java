package com.estore.order_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.estore.order_service.dto.OrderRequest;
import com.estore.order_service.model.Order;
import com.estore.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    
    public void placeOrder(OrderRequest orderRequest) {
        // Map OrderRequest to Order object
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setId(orderRequest.id());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());

        // save to db using repo
        orderRepository.save(order);
    }
}
