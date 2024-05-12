package com.spotit.backend.domain.employee.portfolio;

public class InvalidUserException extends IllegalArgumentException {

    public InvalidUserException() {
        super("User account lacks necessary data!");
    }
}
