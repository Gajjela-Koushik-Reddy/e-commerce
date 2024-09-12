package com.estore.product_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@Entity
@Table(name = "imageData")
@NoArgsConstructor
public class ImageEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String imageId;

        @Column(nullable = false)
        private String productId;

        private String imageName;

        @Lob
        @Column(name = "imagedata")
        private byte[] imageData;
        
        @Column(updatable = false)
        @CreatedDate
        private LocalDateTime uploadedTime;

        @CreatedBy
        private String createdBy;
}
