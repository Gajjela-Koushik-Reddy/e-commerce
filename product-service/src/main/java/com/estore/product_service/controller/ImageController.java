package com.estore.product_service.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.entities.ImageEntity;
import com.estore.product_service.service.impl.ImageServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(path = "/image")
public class ImageController {

    @Autowired
    private ImageServiceImpl imageServiceImpl;

    @PostMapping
    public String uploadImages(@RequestParam("images") MultipartFile[] files) throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        String ret = imageServiceImpl.uploadImages(files);

        return ret;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable("id") String imageId) {

        byte[] image = imageServiceImpl.downloadImage(imageId);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductImages(@PathVariable String productId) {

        List<ImageEntity> productImages = imageServiceImpl.getAllProductImages(productId);

        List<byte[]> images = productImages.stream().map(ImageEntity::getImageData).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(images);

    }

}
