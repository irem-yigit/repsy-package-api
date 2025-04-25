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
import java.util.List;

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

    // Upload file
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();                   // Get file name and path
            String storagePath = "files/" + fileName;                       // File path in bucket for MinIO
            byte[] data = file.getBytes();                                  // Get file data as byte[]

            storageService.save(storagePath, data);                         // Save file to storage (MinIO or local file system)

            String checksum = fileMetadataService.calculateChecksum(file);  // Calculate checksum

            // Create FileMetadata object and save it to database
            FileMetadata metadata = new FileMetadata(fileName, storagePath, file.getSize(), file.getContentType(), checksum);
            fileMetadataService.saveFileMetadata(file);
            return ResponseEntity.ok("The file was successfully uploaded and metadata was saved: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        } catch (MinioException e) {
            throw new RuntimeException(e);
        }
    }

    // List all File Metadata
    @GetMapping("/all")
    public ResponseEntity<List<FileMetadata>> getAllFiles() {
        return ResponseEntity.ok(fileMetadataService.getAllFileMetadata());
    }

}
