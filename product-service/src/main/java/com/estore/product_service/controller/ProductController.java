package com.estore.product_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.entities.ProductSearch;
import com.estore.product_service.service.impl.ElasticSearchServiceImpl;
import com.estore.product_service.service.impl.ProductServiceImpl;

import co.elastic.clients.elasticsearch.core.SearchResponse;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Autowired
    private ElasticSearchServiceImpl elasticSearchServiceImpl;

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<String> addProduct(
            @RequestParam("categoryId") String catrgoryId,
            @RequestParam("productName") String productName,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("stockQuantity") Integer stockQuantity,
            @RequestParam("file") MultipartFile[] file)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        try {
            productServiceImpl.createProduct(
                    catrgoryId,
                    productName,
                    price,
                    description,
                    shortDescription,
                    stockQuantity,
                    file);
        } catch (Exception e) {
            throw new RuntimeException("Problem Uploading Product Data" + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Upload Successful");
    }

    @GetMapping("/helloworld")
    public String getMethodName() {
        return "Hello World";
    }

    @GetMapping("/findAllProducts")
    public String getProducts() throws IOException {
        @SuppressWarnings("rawtypes")
        SearchResponse<Map> res = elasticSearchServiceImpl.matchAllProductsService();
        System.out.println(res);
        return res.hits().hits().toString();
    }

    @GetMapping("/findProductFuzzy/{partialProdName}")
    public List<ProductSearch> findProdWithName(@PathVariable String partialProdName) throws IOException {

        List<ProductSearch> res = productServiceImpl.fetchProductByName(partialProdName);
        return res;
    }

}
