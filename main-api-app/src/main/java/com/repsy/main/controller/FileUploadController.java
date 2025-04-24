package com.repsy.main.controller;

import com.repsy.service.StorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final StorageService storageService;

    public FileUploadController(@Qualifier("storageService") StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = file.getOriginalFilename();
            byte[] data = file.getBytes();

            storageService.save(filePath, data);

            return ResponseEntity.ok("Dosya başarıyla kaydedildi: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hata oluştu: " + e.getMessage());
        }
    }

}
