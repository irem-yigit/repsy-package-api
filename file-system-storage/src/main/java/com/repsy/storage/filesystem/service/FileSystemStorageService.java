package com.repsy.storage.filesystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemStorageService implements StorageService{

    private final Path baseDir;

    public FileSystemStorageService(@Value("${app.storage.base-dir}") String baseDir) {
        this.baseDir = Paths.get(baseDir).toAbsolutePath().normalize();
    }

    @Override
    public void save(String path, byte[] data) {
        try {
            Path fullPath = baseDir.resolve(path).normalize();
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, data);
        } catch (IOException e) {
            throw new RuntimeException("Dosya kaydedilemedi: " + path, e);
        }
    }

    @Override
    public byte[] read(String path) {
        try {
            Path fullPath = baseDir.resolve(path).normalize();
            return Files.readAllBytes(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Dosya okunamadı: " + path, e);
        }
    }
}
