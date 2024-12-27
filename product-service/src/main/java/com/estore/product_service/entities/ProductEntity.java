package com.estore.product_service.entities;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductEntity {
    @Id
    private String productId;

	@NonNull
	private String categoryId;

	@NonNull
	private String name;

	private String price;

	@Min(value = 0, message = "Stock Quantity Cannot be negative")
	private Integer stockQuantity;

	private boolean inStock;

	private String description;

	private String shortDescription;

	private List<String> imageUrls;

	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;
}
