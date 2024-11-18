package com.estore.product_service.controller;

import java.io.IOException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> uploadImages(@RequestParam("images") MultipartFile[] files) throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        List<String> ret = imageServiceImpl.uploadImages(files);

        return ResponseEntity.status(HttpStatus.OK).body(ret.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable("id") String imageId) {

        byte[] image = imageServiceImpl.downloadImage(imageId);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpeg")).body(image);
    }


}
