package com.spotit.backend.storage.exception;

public class ErrorUploadingFileException extends IllegalArgumentException {

    public ErrorUploadingFileException(String filename) {
        super("Error when uploading file '" + filename + "'!");
    }
}
