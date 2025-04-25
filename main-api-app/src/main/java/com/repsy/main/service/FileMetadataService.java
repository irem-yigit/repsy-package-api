package com.repsy.main.service;

import com.repsy.main.entity.FileMetadata;
import com.repsy.main.repository.FileMetadataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileMetadataService {

    private final FileMetadataRepository repository;

    public FileMetadataService(FileMetadataRepository repository) {
        this.repository = repository;
    }

    public FileMetadata save(FileMetadata metadata) {
        return repository.save(metadata);
    }

    public List<FileMetadata> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public FileMetadata findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /*public boolean existsById(Long id) {
    }

    public void deleteById(Long id) {
    }*/
}
