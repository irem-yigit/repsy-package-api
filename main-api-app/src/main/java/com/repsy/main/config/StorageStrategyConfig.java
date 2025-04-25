package com.repsy.main.config;

import com.repsy.storage.filesystem.service.FileSystemStorageService;
import com.repsy.service.StorageService;
import com.repsy.storage.object.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageStrategyConfig {

    @Value("${storage.strategy}")
    private String strategy;

    @Value("${storage.file.baseDir}")
    private String fileBaseDir;

    // Provide a FileSystemStorageService object to the Spring container
    @Bean
    public FileSystemStorageService fileSystemStorageService() {
        return new FileSystemStorageService(fileBaseDir);
    }

    // Provide an ObjectStorageService object to the Spring container
    @Bean
    public ObjectStorageService objectStorageService(
            @Value("${minio.endpoint}") String endpoint,
            @Value("${minio.accessKey}") String accessKey,
            @Value("${minio.secretKey}") String secretKey,
            @Value("${minio.bucketName}") String bucketName
    ) {
        return new ObjectStorageService(endpoint, accessKey, secretKey, bucketName);
    }

    // Provide a StorageService object to the Spring container
    @Bean
    public StorageService storageService(FileSystemStorageService fileService,
                                         ObjectStorageService objectService) {
        return switch (strategy.toLowerCase()) {
            case "file" -> fileService;
            case "object" -> objectService;
            default -> throw new IllegalArgumentException("Unsupported strategy: " + strategy);
        };
    }

}
