package com.repsy.main.controller;

import com.repsy.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

    private final StorageService storageService;

    public FileDownloadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("filename") String filename) {
        try {
            byte[] data = storageService.read(filename);
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
