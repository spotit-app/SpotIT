package com.spotit.backend.abstraction;

public class ErrorCreatingEntityException extends IllegalArgumentException {

    public ErrorCreatingEntityException() {
        super("Error when creating new entity!");
    }
}
