package com.repsy.main.repository;


import com.repsy.main.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    // Find metadata by file name
    Optional<FileMetadata> findByFileName(String fileName);

    // Find metadata by file size
    Optional<FileMetadata> findBySize(long size);

    // Find metadata by storage path
    Optional<FileMetadata> findByStoragePath(String storagePath);
}
