package com.estore.product_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.estore.product_service.config.MinioConfig;
import com.estore.product_service.service.impl.ImageServiceImpl;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadSnowballObjectsArgs;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private MinioConfig minioConfig;

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private ImageServiceImpl imageServiceImpl;

    private final String BUCKET_NAME = "test-bucket";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(imageServiceImpl, "bucket", BUCKET_NAME);
        lenient().when(minioConfig.minioClient()).thenReturn(minioClient);
    }

    @Test
    void uploadImages_Success() throws Exception {
        // Arrange
        MockMultipartFile[] files = {
                new MockMultipartFile("file1", "test1.jpeg", "image/jpeg", "test1 file content".getBytes()),
                new MockMultipartFile("file2", "test2.jpeg", "image/jpeg", "test2 file content".getBytes())
        };

        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        when(minioClient.uploadSnowballObjects(any(UploadSnowballObjectsArgs.class))).thenReturn(null);

        // Act
        List<String> result = imageServiceImpl.uploadImages(files);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).endsWith("test1.jpeg"));
        assertTrue(result.get(1).endsWith("test2.jpeg"));

        verify(minioClient).uploadSnowballObjects(any(UploadSnowballObjectsArgs.class));
    }

    @Test
    void uploadImages_MakeBucket() throws Exception {
        // Arrange
        MockMultipartFile[] images = {
                new MockMultipartFile("file1", "image1.jpeg", "image/jpeg", "image1 content".getBytes())
        };

        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);
        doNothing().when(minioClient).makeBucket(any(MakeBucketArgs.class));
        when(minioClient.uploadSnowballObjects(any(UploadSnowballObjectsArgs.class))).thenReturn(null);

        // Act
        List<String> result = imageServiceImpl.uploadImages(images);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).endsWith("image1.jpeg"));
        verify(minioClient).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient).uploadSnowballObjects(any(UploadSnowballObjectsArgs.class));

    }

    @Test
    void uploadImages_EmptyFile_ThrowsException() throws Exception {
        // Arrange
        MockMultipartFile[] images = new MockMultipartFile[0];

        // Act && Assert
        Exception exception = assertThrows(RuntimeException.class, () -> imageServiceImpl.uploadImages(images));
        assertEquals("Cannot Upload Empty Files", exception.getMessage());
        verify(minioClient, never()).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient, never()).uploadSnowballObjects(any(UploadSnowballObjectsArgs.class));
    }

    @Test
    void uploadImages_Minio_ThrowsException() throws Exception {
        // Arrange
        MockMultipartFile[] images = {
                new MockMultipartFile("file1", "image1.jpeg", "image/jpeg", "image1 content".getBytes())
        };

        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenThrow(new RuntimeException("minio error"));

        // Act && assert
        assertThrows(RuntimeException.class, () -> imageServiceImpl.uploadImages(images));
    }

    @Test
    void downloadImage() throws Exception {
        // Arrange
        String imageUuid = "test-image-uuid";
        byte[] expectedContent = "image data".getBytes();

        GetObjectResponse mockResponse = mock(GetObjectResponse.class);
        when(mockResponse.readAllBytes()).thenReturn(expectedContent);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(mockResponse);

        // Act
        byte[] result = imageServiceImpl.downloadImage(imageUuid);

        // Assert
        assertNotNull(result);
        assertEquals(expectedContent, result, "The expectedContent and result should be same");
        verify(minioClient).getObject(any(GetObjectArgs.class));
        verify(minioClient, times(1))
                .getObject(argThat(args -> args.bucket().equals(BUCKET_NAME) && args.object().equals(imageUuid)));
    }

    @Test
    void uploadImages_NullPointerException() {
        // Arrange
        MockMultipartFile[] files = null;

        // Act && Assert
        assertThrows(NullPointerException.class, () -> imageServiceImpl.uploadImages(files));
    }

    @Test
    void uploadImages_InvalidFile_ThrowsException() {
        // Arrange
        MockMultipartFile[] files = { new MockMultipartFile("file", "test.docx", "", (byte[]) null) };

        // Act && Assert
        assertThrows(RuntimeException.class, () -> imageServiceImpl.uploadImages(files));
    }
}
