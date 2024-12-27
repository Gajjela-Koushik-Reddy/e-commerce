package com.estore.product_service.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.estore.product_service.entities.ProductSearch;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearch, String> {

}
