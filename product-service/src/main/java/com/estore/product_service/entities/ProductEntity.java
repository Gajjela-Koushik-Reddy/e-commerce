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

	public String description;

	public String shortDescription;

	private List<String> imageUrls;

	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;
}
