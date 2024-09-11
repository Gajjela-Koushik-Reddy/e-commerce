package com.estore.product_service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Document("products")
@AllArgsConstructor
@Getter
@Setter
public class ProductEntity {
    @Id
    public String product_id;
	public String prod_category_id;
	public String prod_name;
	public String price;
	public String sku_code;
	// public String Images;
	public String Description;
	// public String created_at;
	// public String updated_at;
}
