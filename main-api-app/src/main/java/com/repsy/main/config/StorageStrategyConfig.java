package com.repsy.main.config;

import com.repsy.storage.filesystem.service.FileSystemStorageService;
import com.repsy.service.StorageService;
import com.repsy.storage.object.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageStrategyConfig {

    @Value("${storage.file.baseDir}")
    private String fileBaseDir;

    @Bean
    public FileSystemStorageService fileSystemStorageService() {
        return new FileSystemStorageService(fileBaseDir);
    }

    @Bean
    public ObjectStorageService objectStorageService(
            @Value("${minio.endpoint}") String endpoint,
            @Value("${minio.accessKey}") String accessKey,
            @Value("${minio.secretKey}") String secretKey,
            @Value("${minio.bucketName}") String bucketName
    ) {
        return new ObjectStorageService(endpoint, accessKey, secretKey, bucketName);
    }

    @Bean
    public StorageService storageService(FileSystemStorageService fileService,
                                         ObjectStorageService objectService) {
        return switch (fileBaseDir.toLowerCase()) {
            case "file" -> fileService;
            case "object" -> objectService;
            default -> throw new IllegalArgumentException("Desteklenmeyen strategy: " + fileBaseDir);
        };
    }
}
