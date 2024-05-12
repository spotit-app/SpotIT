package com.spotit.backend.abstraction;

import java.util.NoSuchElementException;

public class EntityNotFoundException extends NoSuchElementException {

    public EntityNotFoundException(Object id) {
        super("Entity with ID '" + id + "' not found!");
    }
}
