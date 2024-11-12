CREATE TABLE IF NOT EXISTS ProductImageData (
    image_id UUID PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL,
    image_name VARCHAR(255),
    image_data BYTEA,
    uploadedTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    createdBy VARCHAR(255)
);