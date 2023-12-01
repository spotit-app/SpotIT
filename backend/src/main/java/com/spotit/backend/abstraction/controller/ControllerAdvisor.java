package com.spotit.backend.abstraction.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.spotit.backend.abstraction.exception.EntityNotFoundException;
import com.spotit.backend.abstraction.exception.ErrorCreatingEntityException;
import com.spotit.backend.storage.exception.ErrorDeletingFileException;
import com.spotit.backend.storage.exception.ErrorUploadingFileException;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ErrorCreatingEntityException.class)
    public ResponseEntity<String> handleErrorCreatingEntityException(ErrorCreatingEntityException exception) {
        return new ResponseEntity<>(exception.getMessage(), UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ErrorUploadingFileException.class)
    public ResponseEntity<String> handleErrorUploadingFileException(ErrorUploadingFileException exception) {
        return new ResponseEntity<>(exception.getMessage(), UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ErrorDeletingFileException.class)
    public ResponseEntity<String> handleErrorDeletingFileException(ErrorDeletingFileException exception) {
        return new ResponseEntity<>(exception.getMessage(), UNPROCESSABLE_ENTITY);
    }
}
