package com.repsy.main.repository;


import com.repsy.main.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    Optional<FileMetadata> findByFileName(String fileName);

    Optional<FileMetadata> findBySize(long size);

    Optional<FileMetadata> findByStoragePath(String storagePath);
}
