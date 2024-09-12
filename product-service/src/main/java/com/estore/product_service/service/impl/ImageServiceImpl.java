package com.estore.product_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.entities.ImageEntity;
import com.estore.product_service.repository.ImageRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.List;

@Service
public class ImageServiceImpl {

    @Autowired
    private ImageRepository imageRepository;

    public String uploadImage(MultipartFile file) throws IOException {

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageName(file.getName());
        imageEntity.setProductId("1234");
        imageEntity.setImageData(file.getBytes());

        imageRepository.save(imageEntity);

        return new String(file.getOriginalFilename() + "has been uploaded successfully");
    }

    public byte[] downloadImage(String imageId) {
        Optional<ImageEntity> imageEntity = imageRepository.findById(imageId);
        return imageEntity.get().getImageData();
    }

    public List<ImageEntity> getAllProductImages(String productId) {
        return imageRepository.findByProductId(productId);
    }

}