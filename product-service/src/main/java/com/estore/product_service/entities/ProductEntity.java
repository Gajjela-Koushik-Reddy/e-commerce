package com.estore.product_service.entities;

import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import jakarta.persistence.Version;
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
    public String productId;

	@NonNull
	public String categoryId;

	@NonNull
	public String name;

	public String price;

	public String discountedPrice;

	public String skuCode;

	public String upcCode;

	public String description;

	public String shortDescription;

	private String brand;

	private Integer stockQuantity;

	private Integer lowStockThreshold;

	private Boolean isActive;

	private ProductStatus status;

	private Set<String> tags;

	private List<String> imageUrls;

	// Physical attributes
    private Double weight; // in kg
    private Double length; // in cm
    private Double width;  // in cm
    private Double height; // in cm

	// SEO fields
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;

	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;

	@Version
	private Long version;

	// Enum for product status
    public enum ProductStatus {
        DRAFT,
        ACTIVE,
        INACTIVE,
        OUT_OF_STOCK,
        DISCONTINUED
    }
}
