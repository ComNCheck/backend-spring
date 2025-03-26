package com.ComNCheck.ComNCheck.domain.global.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}