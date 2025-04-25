package com.repsy.service;

public interface StorageService {

    // Save file to storage
    void save(String path, byte[] data);

    // Read file from storage
    byte[] read(String path);
}
