package com.repsy.main.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String storagePath;

    private long size;

    @CreationTimestamp
    private LocalDateTime uploadTime;

    @Column(nullable = false)
    private String fileType;

    private String checksum;

    public FileMetadata() {
    }

    // Constructor
    public FileMetadata(String fileName, String storagePath, long size, String fileType, String checksum) {
        this.fileName = fileName;
        this.storagePath = storagePath;
        this.size = size;
        this.uploadTime = LocalDateTime.now();
        this.fileType = fileType;
        this.checksum = checksum;
    }

}
