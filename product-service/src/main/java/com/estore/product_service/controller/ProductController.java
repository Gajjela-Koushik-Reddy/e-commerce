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

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return new String("<h2>Hello World</h2>");
    }

}
