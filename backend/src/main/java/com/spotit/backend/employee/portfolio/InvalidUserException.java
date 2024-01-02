package com.spotit.backend.employee.portfolio;

public class InvalidUserException extends IllegalArgumentException {

    public InvalidUserException() {
        super("User account lacks necessary data!");
    }
}
