package com.repsy.main.controller;

import com.repsy.main.entity.FileMetadata;
import com.repsy.main.service.FileMetadataService;
import com.repsy.service.StorageService;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final StorageService storageService;
    private final FileMetadataService fileMetadataService;

    public FileUploadController(@Qualifier("storageService") StorageService storageService,
                                FileMetadataService fileMetadataService) {
        this.storageService = storageService;
        this.fileMetadataService = fileMetadataService;
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Dosya ismini ve yolu almak
            String fileName = file.getOriginalFilename();
            String storagePath = "files/" + fileName;    // Bu path, MinIO için bucket içinde dosya yolunu ifade eder
            byte[] data = file.getBytes();      // Dosyanın verilerini byte[] olarak alıyoruz

            // Dosyayı storage'a kaydetme (MinIO veya lokal dosya sistemi)
            storageService.save(storagePath, data);

            // Checksum hesapla
            String checksum = fileMetadataService.calculateChecksum(file);

            // FileMetadata objesini oluşturup veritabanına kaydetme
            FileMetadata metadata = new FileMetadata(fileName, storagePath, file.getSize(), file.getContentType(), checksum);
            fileMetadataService.saveFileMetadata(file);
            return ResponseEntity.ok("Dosya başarıyla yüklendi ve metadata kaydedildi: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Dosya yükleme başarısız: " + e.getMessage());
        } catch (MinioException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileMetadata>> getAllFiles() {
        return ResponseEntity.ok(fileMetadataService.getAllFileMetadata());
    }


}
