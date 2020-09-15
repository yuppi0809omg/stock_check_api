package com.example.stock_check_api.exception;

public class UserNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 8258513411717996223L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
