package com.spotit.backend.abstraction.exception;

public class ErrorCreatingEntityException extends IllegalArgumentException {

    public ErrorCreatingEntityException() {
        super("Error when creating new entity!");
    }
}
