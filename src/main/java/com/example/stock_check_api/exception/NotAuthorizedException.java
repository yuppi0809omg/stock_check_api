package com.example.stock_check_api.exception;

public class NotAuthorizedException extends RuntimeException {
    private static final long serialVersionUID = 4897382739117327701L;

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
