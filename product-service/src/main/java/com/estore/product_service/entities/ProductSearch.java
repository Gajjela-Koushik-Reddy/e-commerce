package com.estore.product_service.entities;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "products")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@JsonIgnoreProperties("_class")
public class ProductSearch {
    private String id;
    private String productName;
    private String shortDescription;
    private String price;
}