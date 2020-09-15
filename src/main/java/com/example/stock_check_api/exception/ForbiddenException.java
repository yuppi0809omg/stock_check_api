package com.example.stock_check_api.exception;

public class ForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 4897382739117327701L;

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

}
