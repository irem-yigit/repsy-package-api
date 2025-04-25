package com.repsy.main.controller;

import com.repsy.main.entity.FileMetadata;
import com.repsy.main.service.FileMetadataService;
import com.repsy.service.StorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final StorageService storageService;
    private final FileMetadataService fileMetadataService;

    public FileUploadController(@Qualifier("storageService") StorageService storageService, FileMetadataService fileMetadataService) {
        this.storageService = storageService;
        this.fileMetadataService = fileMetadataService;
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String storagePath = "files/" + fileName;
            byte[] data = file.getBytes();

            storageService.save(storagePath, data);

            FileMetadata metadata = new FileMetadata(fileName, storagePath, file.getSize());
            fileMetadataService.save(metadata);

            return ResponseEntity.ok("Dosya başarıyla yüklendi ve metadata kaydedildi: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Dosya yükleme başarısız: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileMetadata>> getAllFiles() {
        return ResponseEntity.ok(fileMetadataService.findAll());
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<?> getFileMetadata(@PathVariable Long id) {
        Optional<FileMetadata> metadata = Optional.ofNullable(fileMetadataService.findById(id));
        return metadata.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Metadata bulunamadı"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMetadata(@PathVariable Long id) {
        if (fileMetadataService.existsById(id)) {
            fileMetadataService.deleteById(id);
            return ResponseEntity.ok("Metadata başarıyla silindi.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Metadata bulunamadı");
        }
    }*/

}
