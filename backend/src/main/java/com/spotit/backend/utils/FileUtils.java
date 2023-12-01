package com.spotit.backend.utils;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUtils {

    static byte[] getBytesFromFile(MultipartFile file) {
        byte[] bytes = null;

        if (file != null) {
            try {
                bytes = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }
}
