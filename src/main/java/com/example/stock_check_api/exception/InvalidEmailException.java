package com.example.stock_check_api.exception;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }

}
