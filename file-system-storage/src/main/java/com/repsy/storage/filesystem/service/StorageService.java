package com.repsy.storage.filesystem.service;

public interface StorageService {

    void save(String path, byte[] data);
    byte[] read(String path);
}
