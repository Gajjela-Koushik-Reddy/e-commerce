package com.estore.product_service.service.impl;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.entities.ProductEntity;
import com.estore.product_service.repository.ProductRepository;

@Service
public class ProductServiceImpl {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageServiceImpl imageServiceImpl;

    public boolean addProduct(MultipartFile[] images)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        ProductEntity newProduct = new ProductEntity();

        newProduct.setProductId(UUID.randomUUID().toString());
        newProduct.setCategoryId(UUID.randomUUID().toString());
        newProduct.setName("something");
        newProduct.setPrice("1000");
        newProduct.setDescription("this is the description of the product");
        newProduct.setShortDescription("this is short des");
        try {
            List<String> imageStrings = imageServiceImpl.uploadImages(images);
            newProduct.setImageUrls(imageStrings);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        newProduct.setCreatedAt(LocalDateTime.now());
        productRepository.save(newProduct);
        return false;
    }
}
