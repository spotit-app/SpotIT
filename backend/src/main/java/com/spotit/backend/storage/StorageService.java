package com.spotit.backend.storage;

public interface StorageService {

    String uploadFile(byte[] file, String directory, String filename);

    void deleteFile(String filename);
}
