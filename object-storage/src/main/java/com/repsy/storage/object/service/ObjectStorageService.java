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

    // Constructor üzerinden MinIO'ya bağlanacak bilgileri alıyoruz.
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

    @Override
    public void save(String path, byte[] data) {
        try {
            // MinIO'ya dosya kaydediyoruz
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)       // Dosya adı/path
                            .stream(new ByteArrayInputStream(data), data.length, -1) // Veriyi byte array olarak aktarıyoruz
                            .contentType("application/octet-stream")    // İçerik tipi (genellikle binary)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("MinIO'ya kaydetme hatası: " + path, e);
        }
    }

    @Override
    public byte[] read(String path) {
        try (io.minio.GetObjectResponse stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(path)   // Okumak istediğimiz dosya adı/path
                        .build())) {
            return stream.readAllBytes();       // Dosyayı byte[] olarak okuyoruz
        } catch (Exception e) {
            throw new RuntimeException("MinIO'dan okuma hatası: " + path, e);
        }
    }
}
