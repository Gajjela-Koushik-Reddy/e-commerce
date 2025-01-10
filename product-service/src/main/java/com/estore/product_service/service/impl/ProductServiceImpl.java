package com.estore.product_service.service.impl;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.dto.ProductResponseDto;
import com.estore.product_service.entities.ProductEntity;
import com.estore.product_service.entities.ProductSearch;
import com.estore.product_service.repository.ProductRepository;
import com.estore.product_service.repository.ProductSearchRepository;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private ProductSearchRepository productSearchRepository;

        @Autowired
        private ImageServiceImpl imageServiceImpl;

        @Autowired
        private ElasticSearchServiceImpl elasticSearchServiceImpl;

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
                if (catrgoryId == null || catrgoryId.isEmpty())
                        throw new InvalidParameterException("Category Id cannot be empty or null");
                if (productName == null || productName.isEmpty())
                        throw new InvalidParameterException("product name cannot be empty or null");
                if (price == null || price.isEmpty())
                        throw new InvalidParameterException("price cannot be empty or null");
                if (description == null || description.isEmpty())
                        throw new InvalidParameterException("description cannot be empty");
                if (shortDescription == null || shortDescription.isEmpty())
                        throw new InvalidParameterException("short description cannot be empty");
                if (stockQuantity == null || stockQuantity.intValue() <= 0)
                        throw new InvalidParameterException("stock quantity cannot be less than 0");

                ProductEntity newProduct = new ProductEntity();
                String productId = UUID.randomUUID().toString();
                newProduct.setProductId(productId);
                newProduct.setCategoryId(catrgoryId);
                newProduct.setName(productName);
                newProduct.setPrice(price);
                newProduct.setDescription(description);
                newProduct.setShortDescription(shortDescription);
                newProduct.setStockQuantity(stockQuantity);
                newProduct.setInStock(true);
                newProduct.setCreatedAt(LocalDateTime.now());

                ProductSearch newProductSearch = ProductSearch.builder()
                                .id(productId)
                                .productName(productName)
                                .shortDescription(shortDescription)
                                .price(price)
                                .build();

                try {
                        List<String> imageStrings = imageServiceImpl.uploadImages(images);
                        newProduct.setImageUrls(imageStrings);
                        productRepository.save(newProduct);
                        productSearchRepository.save(newProductSearch);
                } catch (Exception e) {
                        throw new RuntimeException("Error creating the product" + e.getMessage());
                }
        }

        public List<ProductSearch> fetchProductByName(String productName) throws IOException {
                try {
                        SearchResponse<ProductSearch> response = elasticSearchServiceImpl
                                        .matchProductsByName(productName);
                        List<ProductSearch> ret = new ArrayList<>();

                        ret = response.hits().hits().stream().map(s -> s.source()).collect(Collectors.toList());

                        return ret;
                } catch (Exception e) {
                        throw new RuntimeException("cannot fetch product name");
                }
        }

        public ProductResponseDto fetchProductById(String id) {

                ProductResponseDto ret;
                try {
                        Optional<ProductEntity> res = productRepository.findById(id);
                        if (res.isPresent()) {
                                ProductEntity prod = res.get();
                                ret = new ProductResponseDto(
                                                prod.getProductId(),
                                                prod.getName(),
                                                prod.getPrice(),
                                                prod.getDescription(),
                                                prod.getImageUrls());
                        } else {
                                throw new RuntimeException("Cannot find the product with id: " + id);
                        }
                } catch (Exception e) {
                        throw new RuntimeException("Cannot find the product with id: " + id + e.getMessage());
                }

                return ret;
        }

        public void updateProduct(
                        String productId,
                        String categoryId,
                        String productName,
                        String price,
                        String description,
                        String shortDescription,
                        Integer stockQuantity,
                        MultipartFile[] images) {

                if (productId == null || productId.isEmpty())
                        throw new InvalidParameterException("productId cannot be null or empty");

                Optional<ProductEntity> existingProductOptional = productRepository.findById(productId);

                if (existingProductOptional.isEmpty())
                        throw new RuntimeException("Cannot find the product with productid" + productId);

                ProductEntity existingProduct = existingProductOptional.get();

                if (categoryId != null && !categoryId.isEmpty())
                        existingProduct.setCategoryId(categoryId);
                if (productName != null && !productName.isEmpty())
                        existingProduct.setName(productName);
                if (price != null && !price.isEmpty())
                        existingProduct.setPrice(price);
                if (description != null && !description.isEmpty())
                        existingProduct.setDescription(description);
                if (shortDescription != null && !shortDescription.isEmpty())
                        existingProduct.setShortDescription(shortDescription);
                if (stockQuantity != null) {
                        if (stockQuantity.intValue() < 0)
                                throw new InvalidParameterException("stock quantity cannot be less than 0");
                        existingProduct.setStockQuantity(stockQuantity);
                }

                if (images != null && images.length > 0) {
                        try {
                                List<String> imageuuids = imageServiceImpl.uploadImages(images);
                                existingProduct.setImageUrls(imageuuids);
                        } catch (Exception e) {
                                throw new RuntimeException("Error uploading images" + e.getMessage());
                        }
                }

                try {
                        existingProduct.setLastModifiedAt(LocalDateTime.now());
                        productRepository.save(existingProduct);
                } catch (Exception e) {
                        throw new RuntimeException("Cannot save product" + e.getMessage());
                }

        }

        public void deleteProduct(String productId) {
                Optional<ProductEntity> existingProductOptional = productRepository.findById(productId);
                if (existingProductOptional.isEmpty())
                        throw new RuntimeException("cannot find the product with product id" + productId);

                productRepository.deleteById(productId);
        }
}
