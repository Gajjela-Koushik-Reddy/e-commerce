package com.estore.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.product_service.entities.ImageEntity;
import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, String> {

    List<ImageEntity> findByProductId(String productId);
}
