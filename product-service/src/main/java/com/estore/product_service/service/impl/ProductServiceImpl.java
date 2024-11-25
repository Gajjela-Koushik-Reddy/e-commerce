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
            MultipartFile[] images)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        // Validate inputs
        if(catrgoryId.isEmpty() || catrgoryId == null)
                throw new InvalidParameterException("Category Id cannot be empty or null");
        if(productName.isEmpty() || productName == null)
                throw new InvalidParameterException("product name cannot be empty or null");
        if(price.isEmpty() || price == null)
                throw new InvalidParameterException("price cannot be empty or null");
        if(description.isEmpty() || description == null)
                throw new InvalidParameterException("description cannot be empty");
        if(shortDescription.isEmpty() || shortDescription == null)
                throw new InvalidParameterException("short descriptiion cannot be empty");

        ProductEntity newProduct = new ProductEntity();

        newProduct.setProductId(UUID.randomUUID().toString());
        newProduct.setCategoryId(catrgoryId);
        newProduct.setName(productName);
        newProduct.setPrice(price);
        newProduct.setDescription(description);
        newProduct.setShortDescription(shortDescription);
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
