package com.repsy.main.controller;

import com.repsy.main.entity.FileMetadata;
import com.repsy.main.service.FileMetadataService;
import io.minio.errors.MinioException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileMetadataController {

    private final FileMetadataService fileMetadataService;

    public FileMetadataController(FileMetadataService fileMetadataService) {
        this.fileMetadataService = fileMetadataService;
    }

    // Tüm dosyaları getir
    @GetMapping
    public List<FileMetadata> getAllFiles() {
        return fileMetadataService.getAllFileMetadata();
    }

    // Belirli ID ile dosya getir
    @GetMapping("/{id}")
    public ResponseEntity<FileMetadata> getFileById(@PathVariable Long id) {
        Optional<FileMetadata> file = Optional.ofNullable(fileMetadataService.getFileMetadataById(id));
        return file.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Yeni dosya kaydet
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Dosya yükleme ve metadata bilgilerini kaydetme
            fileMetadataService.saveFileMetadata(file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException | MinioException e) {
            return ResponseEntity.status(500).body("Failed to upload file");
        }
    }

    // Silme işlemi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        if (fileMetadataService.deleteFileMetadata(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
