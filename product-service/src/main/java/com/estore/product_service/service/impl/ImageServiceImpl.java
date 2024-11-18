package com.estore.product_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.estore.product_service.config.MinioConfig;
import com.estore.product_service.entities.ImageEntity;
import com.estore.product_service.repository.ImageRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SnowballObject;
import io.minio.UploadSnowballObjectsArgs;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class ImageServiceImpl {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MinioConfig minioConfig;

    @Value("${minio.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile file) throws IOException {

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageName(file.getName());
        imageEntity.setProductId("1234");
        imageEntity.setImageData(file.getBytes());

        System.out.println(file.getContentType());

        imageRepository.save(imageEntity);

        return new String(file.getOriginalFilename() + "has been uploaded successfully");
    }

    public List<String> uploadFilesToMinio(MultipartFile[] files)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        MinioClient minioClient = minioConfig.minioClient();
        List<String> imageuuids = new ArrayList<>();

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            // bucket exists
            // upload all the files
            List<SnowballObject> objects = new ArrayList<>();
            for (MultipartFile image : files) {
                String filename = UUID.randomUUID() + image.getOriginalFilename();
                imageuuids.add(filename);
                objects.add(new SnowballObject(filename,
                        new ByteArrayInputStream(image.getBytes()), image.getSize(), null));
            }
            minioClient.uploadSnowballObjects(
                    UploadSnowballObjectsArgs.builder().bucket(bucket).objects(objects).build());

            return imageuuids;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public String uploadImages(MultipartFile[] images)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        uploadFilesToMinio(images);
        return "NotImplementedYet";
    }

    public byte[] downloadImage(String imageId) {
        Optional<ImageEntity> imageEntity = imageRepository.findById(imageId);
        return imageEntity.get().getImageData();
    }

    public List<ImageEntity> getAllProductImages(String productId) {
        return imageRepository.findByProductId(productId);
    }

}