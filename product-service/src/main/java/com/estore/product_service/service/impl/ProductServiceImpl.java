package com.estore.product_service.service.impl;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.entities.ProductEntity;
import com.estore.product_service.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageServiceImpl imageServiceImpl;

    @Transactional
    public void createProduct(
            String catrgoryId,
            String productName,
            String price,
            String description,
            String shortDescription,
            Integer stockQuantity,
            MultipartFile[] images)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        // Validate inputs
        if(catrgoryId == null || catrgoryId.isEmpty())
                throw new InvalidParameterException("Category Id cannot be empty or null");
        if(productName == null || productName.isEmpty())
                throw new InvalidParameterException("product name cannot be empty or null");
        if(price == null || price.isEmpty())
                throw new InvalidParameterException("price cannot be empty or null");
        if(description == null || description.isEmpty())
                throw new InvalidParameterException("description cannot be empty");
        if(shortDescription == null || shortDescription.isEmpty())
                throw new InvalidParameterException("short description cannot be empty");
        if(stockQuantity == null || stockQuantity.intValue() <= 0)
                throw new InvalidParameterException("stock quantity cannot be less than 0");

        ProductEntity newProduct = new ProductEntity();

        newProduct.setProductId(UUID.randomUUID().toString());
        newProduct.setCategoryId(catrgoryId);
        newProduct.setName(productName);
        newProduct.setPrice(price);
        newProduct.setDescription(description);
        newProduct.setShortDescription(shortDescription);
        newProduct.setStockQuantity(stockQuantity);
        newProduct.setInStock(true);
        newProduct.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));

        try {
            List<String> imageStrings = imageServiceImpl.uploadImages(images);
            newProduct.setImageUrls(imageStrings);
            productRepository.save(newProduct);
        } catch (Exception e) {
            throw new RuntimeException("Error creating the product" + e.getMessage());
        }
    }
}
