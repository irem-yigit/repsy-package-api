package com.repsy.storage.object.service;

import com.repsy.service.StorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;


public class ObjectStorageService implements StorageService {

    private final MinioClient minioClient;
    private final String bucketName;

    // Get the information to connect to MinIO via Constructor
    public ObjectStorageService(
            @Value("${minio.endpoint}") String endpoint,
            @Value("${minio.accessKey}") String accessKey,
            @Value("${minio.secretKey}") String secretKey,
            @Value("${minio.bucketName}") String bucketName
    ) {
        this.bucketName = bucketName;
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    // Save file to MinIO
    @Override
    public void save(String path, byte[] data) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(new ByteArrayInputStream(data), data.length, -1)
                            .contentType("application/octet-stream")
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving to MinIO: " + path, e);
        }
    }

    // Read file from MinIO
    @Override
    public byte[] read(String path) {
        try (io.minio.GetObjectResponse stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(path)
                        .build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error reading from MinIO: " + path, e);
        }
    }
}
