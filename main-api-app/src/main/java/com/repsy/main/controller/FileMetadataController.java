package com.repsy.main.controller;

import com.repsy.main.entity.FileMetadata;
import com.repsy.main.repository.FileMetadataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileMetadataController {

    private final FileMetadataRepository repository;

    public FileMetadataController(FileMetadataRepository repository) {
        this.repository = repository;
    }

    // 1️⃣ Tüm dosyaları getir
    @GetMapping
    public List<FileMetadata> getAllFiles() {
        return repository.findAll();
    }

    // 2️⃣ Belirli ID ile dosya getir
    @GetMapping("/{id}")
    public ResponseEntity<FileMetadata> getFileById(@PathVariable Long id) {
        Optional<FileMetadata> file = repository.findById(id);
        return file.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3️⃣ Yeni dosya kaydet
    @PostMapping
    public FileMetadata saveFile(@RequestBody FileMetadata fileMetadata) {
        return repository.save(fileMetadata);
    }

    // 4️⃣ Silme işlemi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
