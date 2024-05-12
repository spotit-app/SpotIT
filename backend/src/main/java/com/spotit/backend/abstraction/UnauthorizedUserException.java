package com.spotit.backend.abstraction;

import org.springframework.security.access.AccessDeniedException;

public class UnauthorizedUserException extends AccessDeniedException {

    public UnauthorizedUserException() {
        super("Unauthorized access to the resource!");
    }
}
