package com.estore.product_service.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estore.product_service.entities.ProductSearch;
import com.estore.product_service.utils.ElasticsearchUtil;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;

@Service
public class ElasticSearchServiceImpl {
    
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    
    @SuppressWarnings("rawtypes")
    public SearchResponse<Map> matchAllService() throws IOException {
        
        Supplier<Query> supplier = ElasticsearchUtil.MatchAllQuerySupplier();
        SearchResponse<Map> searchResponse = elasticsearchClient.search(s->s.query(supplier.get()), Map.class);
        System.out.println("elastic search query "+supplier.get().toString());
        return searchResponse;
    }

    @SuppressWarnings("rawtypes")
    public SearchResponse<Map> matchAllProductsService() throws IOException {
        
        Supplier<Query> supplier = ElasticsearchUtil.MatchAllQuerySupplier();
        SearchResponse<Map> searchResponse = elasticsearchClient.search(s->s.index("products").query(supplier.get()), Map.class);
        System.out.println("elastic search query for all products "+supplier.get().toString());
        return searchResponse;
    }

    public SearchResponse<ProductSearch> matchProductsByName(String productName) throws IOException {
        
        Supplier<Query> supplier = ElasticsearchUtil.MatchProdByNameSupplier(productName);
        SearchResponse<ProductSearch> searchResponse = elasticsearchClient.search(s->s.index("products").query(supplier.get()), ProductSearch.class);
        System.out.println("elastic search query for all products "+supplier.get().toString());
        return searchResponse;
    }

}
