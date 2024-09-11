package com.estore.product_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.estore.product_service.entities.ProductEntity;
// import com.estore.product_service.repository.ProductRepository;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class ProductController {

    // private final ProductRepository productRepository;

    @GetMapping("/helloWorld")
    public String helloWorld() {
        // productRepository.save(new ProductEntity("Product 1", "Category 1", "Laptop", "$1000", "Description 1"));
        // productRepository.save(new ProductEntity("Product 2", "Category 2", "Phone", "$800", "Description 2"));
        // productRepository.save(new ProductEntity("Product 3", "Category 1", "Tablet", "$500", "Description 3"));
        return new String("<h2>Hello World</h2>");
    }

}
