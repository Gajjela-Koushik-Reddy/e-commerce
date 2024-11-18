package com.estore.product_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.service.impl.ProductServiceImpl;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    @PostMapping("/")
    public ResponseEntity<String> addProduct(@PathVariable MultipartFile[] images) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        productServiceImpl.addProduct(images);
        return ResponseEntity.status(HttpStatus.OK).body("Upload Successful");
    }

    @GetMapping("/helloworld")
    public String getMethodName() {
        return "Hello World";
    }
    

}
