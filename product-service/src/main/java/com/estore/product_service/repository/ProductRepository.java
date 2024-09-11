package com.estore.product_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.estore.product_service.entities.ProductEntity;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    
}
