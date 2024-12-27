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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.estore.product_service.entities.ProductEntity;
import com.estore.product_service.repository.ProductRepository;
import com.estore.product_service.service.impl.ImageServiceImpl;
import com.estore.product_service.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageServiceImpl imageServiceImpl;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    void createProduct_Success() throws Exception {

        // Arrange
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };
        List<String> imageUrls = List.of("url1", "url2");

        // Create a mock ProductEntity object to simulate the saved product
        ProductEntity mockProductEntity = new ProductEntity();
        mockProductEntity.setProductId("1234");
        mockProductEntity.setCategoryId(categoryId);
        mockProductEntity.setName(productName);
        mockProductEntity.setPrice(price);
        mockProductEntity.setDescription(description);
        mockProductEntity.setShortDescription(shortDescription);
        mockProductEntity.setStockQuantity(stockQuantity);
        mockProductEntity.setImageUrls(imageUrls);

        when(imageServiceImpl.uploadImages(images)).thenReturn(imageUrls);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(mockProductEntity);
        ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);

        // Act
        productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity,
                images);

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
        assertNotNull(capturedProduct.getCreatedAt());
    }

    @Test
    void createProduct_failedImageUpload() throws Exception {
        // Arrange
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };
        
        when(imageServiceImpl.uploadImages(images)).thenThrow(new RuntimeException("Images are empty"));
        
        // Act
        // Assert
        assertThrows(RuntimeException.class,
                () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription,
                        stockQuantity,
                        images));
        verify(imageServiceImpl, times(1)).uploadImages(images);
    }

    @Test
    void createProduct_nullCategoryId() {
        
        // Arrange 
        String categoryId = null;
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_emptyCategoryId() {
        
        // Arrange 
        String categoryId = "";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_nullProductId() {
        
        // Arrange 
        String categoryId = "1";
        String productName = null;
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_emptyProductId() {
        
        // Arrange 
        String categoryId = "1";
        String productName = "";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_nullPrice() {
        
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = null;
        String description = "test description";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_emptyPrice() {
        
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "";
        String description = "test description";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_nullDescription() {
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = null;
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_emptyDescription() {
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "";
        String shortDescription = "test short description";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_nullShortDescription() {
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = null;
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_emptyShortDescription() {
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "";
        int stockQuantity = 10;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_nullStockQuantity() {
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        Integer stockQuantity = null;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_zeroStockQuantity() {
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        Integer stockQuantity = 0;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }
    
    @Test
    void createProduct_negativeStockQuantity() {
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        Integer stockQuantity = 0;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.jpeg", "image/jpeg", "test image 1 content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.jpeg", "image/jpeg", "test image 2 content".getBytes())
        };

        // Act and Assert
        assertThrows(InvalidParameterException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

    @Test
    void createProduct_invalidFileFormat_throwsException() throws Exception {
        // Arrange 
        String categoryId = "1";
        String productName = "test product name";
        String price = "10.99";
        String description = "test description";
        String shortDescription = "test short description";
        Integer stockQuantity = 5;
        MockMultipartFile[] images = {
                new MockMultipartFile("image 1", "testImage1.txt", "text/plain", "invalid content".getBytes()),
                new MockMultipartFile("image 2", "testImage2.txt", "text/plain", "invalid content".getBytes())
        };

        when(imageServiceImpl.uploadImages(images)).thenThrow(new RuntimeException("invalid imagge format"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> productServiceImpl.createProduct(categoryId, productName, price, description, shortDescription, stockQuantity, images));
    }

}
