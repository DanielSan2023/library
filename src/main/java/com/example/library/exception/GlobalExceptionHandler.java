package com.example.library.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(BookNotFoundException e) {
        log.error("📕 Book not found exception: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Book not Found", e.getMessage(), null);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException e) {
        log.error("❌ Entity not found: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Entity not found", e.getMessage(), null);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException e) {
        log.error("❌ Validation error: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        log.error("❌ Method argument not valid: {}", message, e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request body", message, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException e) {
        log.error("❌ Illegal argument: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad request", e.getMessage(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException e) {
        log.error("❌ Unexpected runtime exception", e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception e) {
        log.error("❌ Unhandled exception", e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", "An unexpected error occurred.", e.getMessage());
    }

    private ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, String error, String message, String details) {
        ApiError response = new ApiError(status.value(), error, message, details, Instant.now());
        return ResponseEntity.status(status).body(response);
    }
}


