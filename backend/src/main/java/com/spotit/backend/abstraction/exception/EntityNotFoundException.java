package com.spotit.backend.abstraction.exception;

import java.util.NoSuchElementException;

public class EntityNotFoundException extends NoSuchElementException {

    public EntityNotFoundException(Object id) {
        super("Entity with ID '" + id + "' not found!");
    }
}
