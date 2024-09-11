package com.estore.product_service.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageEntity {
        private String image_id;
        private String product_id;
        private String image_name;
        private byte[] image_blob;
        private String download_url;
}
