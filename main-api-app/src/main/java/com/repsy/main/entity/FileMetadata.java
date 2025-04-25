package com.repsy.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String storagePath;
    private long size;
    private LocalDateTime uploadTime;

    // Constructors
    public FileMetadata() {
    }

    public FileMetadata(String fileName, String storagePath, long size) {
        this.fileName = fileName;
        this.storagePath = storagePath;
        this.size = size;
        this.uploadTime = LocalDateTime.now();
    }

}
