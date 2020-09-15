package com.example.stock_check_api.exception;

public class EmailFailedToBeSent extends RuntimeException {

    public EmailFailedToBeSent(String message) {
        super(message);
    }

    public EmailFailedToBeSent(String message, Throwable cause) {
        super(message, cause);
    }
}
