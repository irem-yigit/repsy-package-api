package com.repsy.storage.filesystem.service;

import com.repsy.service.StorageService;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileSystemStorageService implements StorageService{

    private final Path baseDir;

    public FileSystemStorageService(String baseDir) {
        this.baseDir = Paths.get(baseDir).toAbsolutePath().normalize();
    }

    // Save the file to the specified file path
    @Override
    public void save(String path, byte[] data) {
        try {
            Path fullPath = baseDir.resolve(path).normalize();
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, data);
        } catch (IOException e) {
            throw new RuntimeException("File could not be saved: " + path, e);
        }
    }

    // Read the file at the specified path and return it as a byte array
    @Override
    public byte[] read(String path) {
        try {
            Path fullPath = baseDir.resolve(path).normalize();
            return Files.readAllBytes(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + path, e);
        }
    }

}
