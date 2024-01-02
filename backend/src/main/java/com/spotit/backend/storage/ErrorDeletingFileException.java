package com.spotit.backend.storage;

public class ErrorDeletingFileException extends IllegalArgumentException {
    public ErrorDeletingFileException(String filename) {
        super("Error while deleting file '" + filename + "'!");
    }
}
