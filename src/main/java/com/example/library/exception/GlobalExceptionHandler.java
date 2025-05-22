package com.example.library.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

import static com.example.library.utility.ErrorMessages.*;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(BookNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND, e.getMessage(), null);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ENTITY_NOT_FOUND, e.getMessage(), null);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED, e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, INVALID_REQUEST_BODY, message, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, e.getMessage(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR, e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR, GENERIC_UNEXPECTED_ERROR, e.getMessage());
    }

    private ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, String error, String message, String details) {
        ApiError response = new ApiError(status.value(), error, message, details, Instant.now());
        return ResponseEntity.status(status).body(response);
    }
}


