package com.ComNCheck.ComNCheck.domain.global.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
