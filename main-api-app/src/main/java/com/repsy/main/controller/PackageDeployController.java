package com.repsy.main.controller;

import com.repsy.service.StorageService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class PackageDeployController {

    private final StorageService storageService;

    public PackageDeployController(@Qualifier("storageService") StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/{packageName}/{version}")
    public ResponseEntity<String> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("meta") MultipartFile metaFile,
            @RequestParam("package") MultipartFile packageFile) {

        try {
            String basePath = String.format("packages/%s/%s/", packageName, version);

            storageService.save(basePath + "meta.json", metaFile.getBytes());
            storageService.save(basePath + "package.rep", packageFile.getBytes());

            return ResponseEntity.ok("Package uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload package: " + e.getMessage());
        }
    }
}
