package com.repsy.main.controller;

import com.repsy.main.entity.FileMetadata;
import com.repsy.main.service.FileMetadataService;
import com.repsy.service.StorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

    private final StorageService storageService;
    private final FileMetadataService fileMetadataService;

    public FileDownloadController(@Qualifier("storageService") StorageService storageService,
                                  FileMetadataService fileMetadataService) {
        this.storageService = storageService;
        this.fileMetadataService = fileMetadataService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("id") Long id) {
        try {
            Optional<FileMetadata> metadataOpt = Optional.ofNullable(fileMetadataService.findById(id));
            if (metadataOpt.isEmpty()) {
                return ResponseEntity.status(404).body(null);
            }

            FileMetadata metadata = metadataOpt.get();
            byte[] data = storageService.read(metadata.getStoragePath());
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getFileName() + "\"")
                    .contentLength(metadata.getSize())
                    .body(resource);

        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
