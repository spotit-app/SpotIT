package com.spotit.backend.employee.abstraction;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.spotit.backend.employee.portfolio.InvalidUserException;
import com.spotit.backend.storage.ErrorDeletingFileException;
import com.spotit.backend.storage.ErrorUploadingFileException;

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

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> handleInvalidUserException(InvalidUserException exception) {
        return new ResponseEntity<>(exception.getMessage(), UNPROCESSABLE_ENTITY);
    }
}
