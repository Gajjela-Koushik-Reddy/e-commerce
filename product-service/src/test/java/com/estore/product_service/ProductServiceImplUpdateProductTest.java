package com.estore.product_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.entities.ProductEntity;
import com.estore.product_service.repository.ProductRepository;
import com.estore.product_service.repository.ProductSearchRepository;
import com.estore.product_service.service.impl.ImageServiceImpl;
import com.estore.product_service.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplUpdateProductTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageServiceImpl imageServiceImpl;

    @Mock
    private ProductSearchRepository productSearchRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    void updateProduct_Success() throws Exception {

        // Arrange
        String productId = "123";
        String categoryId = "1";
        String productName = "laptop";
        String price = "100";
        String description = "description";
        String shortDescription = "short description";
        Integer stockQuantity = 12;
        MultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes()) };
        List<String> imageUrls = List.of("UpdatedUrl1", "UpdatedUrl2");

        // Create a mock product entity to simulate updated product
        ProductEntity mockProductEntity = new ProductEntity();

        mockProductEntity.setProductId(productId);
        mockProductEntity.setCategoryId(categoryId);
        mockProductEntity.setName(productName);
        mockProductEntity.setProductId(productId);
        mockProductEntity.setPrice(price);
        mockProductEntity.setDescription(description);
        mockProductEntity.setShortDescription(shortDescription);
        mockProductEntity.setStockQuantity(stockQuantity);
        mockProductEntity.setImageUrls(imageUrls);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(mockProductEntity));
        when(imageServiceImpl.uploadImages(images)).thenReturn(imageUrls);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(mockProductEntity);
        ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);

        // Act
        productServiceImpl.updateProduct(productId, categoryId, productName, price, description, shortDescription,
                stockQuantity, images);

        // Assert
        verify(imageServiceImpl, times(1)).uploadImages(images);
        verify(productRepository, times(1)).save(productCaptor.capture());

        ProductEntity capturedProduct = productCaptor.getValue();
        assertEquals(capturedProduct.getCategoryId(), categoryId);
        assertEquals(capturedProduct.getName(), productName);
        assertEquals(capturedProduct.getPrice(), price);
        assertEquals(capturedProduct.getDescription(), description);
        assertEquals(capturedProduct.getShortDescription(), shortDescription);
        assertEquals(capturedProduct.getStockQuantity(), stockQuantity);
        assertEquals(capturedProduct.getImageUrls(), imageUrls);
        assertNotNull(capturedProduct.getLastModifiedAt());
    }

    @Test
    void updateProduct_ProductNotFound() throws Exception {

        // Arrange
        String productId = "nonExistentId";
        String categoryId = "1";
        String productName = "laptop";
        String price = "100";
        String description = "description";
        String shortDescription = "short description";
        Integer stockQuantity = 12;
        MultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes()) };

        // Act
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productServiceImpl.updateProduct(productId, categoryId, productName, price, description,
                        shortDescription, stockQuantity, images),
                "The Test Didnt Return a exception as expected");
    }

    @Test
    void updateProduct_NullProductId() throws Exception {

        // Arrange
        String productId = null;
        String categoryId = "1";
        String productName = "laptop";
        String price = "100";
        String description = "description";
        String shortDescription = "short description";
        Integer stockQuantity = 12;
        MultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes()) };

        assertThrows(InvalidParameterException.class,
                () -> productServiceImpl.updateProduct(productId, categoryId, productName, price, description,
                        shortDescription, stockQuantity, images),
                "The Test Didnt Return a exception as expected");
    }
}
