package com.repsy.main.config;

import com.repsy.storage.filesystem.service.FileSystemStorageService;
import com.repsy.storage.filesystem.service.StorageService;
import com.repsy.storage.object.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageStrategyConfig {

    @Value("${storage.strategy}")
    private String strategy;

    private final FileSystemStorageService fileStorageService;
    private final ObjectStorageService objectStorageService;

    public StorageStrategyConfig(FileSystemStorageService fileStorageService,
                                 ObjectStorageService objectStorageService) {
        this.fileStorageService = fileStorageService;
        this.objectStorageService = objectStorageService;
    }

    @Bean
    public StorageService storageService() {
        return switch (strategy.toLowerCase()) {
            case "file" -> fileStorageService;
            case "object" -> objectStorageService;
            default -> throw new IllegalArgumentException("Desteklenmeyen strategy: " + strategy);
        };
    }
}
