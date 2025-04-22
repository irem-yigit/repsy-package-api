package com.repsy.storage.filesystem.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemStorageService implements StorageService{

    private final String baseDir = "storage/files";

    @Override
    public void save(String path, byte[] data) {
        try {
            Path fullPath = Paths.get(baseDir, path);
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, data);
        } catch (IOException e) {
            throw new RuntimeException("Dosya kaydedilemedi: " + path, e);
        }
    }

    @Override
    public byte[] read(String path) {
        try {
            Path fullPath = Paths.get(baseDir, path);
            return Files.readAllBytes(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Dosya okunamadı: " + path, e);
        }
    }
}
