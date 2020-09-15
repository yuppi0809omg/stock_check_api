package com.example.stock_check_api.exception;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8258513411717996223L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
