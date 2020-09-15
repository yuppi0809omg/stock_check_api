package com.example.stock_check_api.exception;

public class UserDuplicationException extends DuplicationException{

    public UserDuplicationException(String message) {
        super(message);
    }

    public UserDuplicationException(String message, String type) {
        super(message, type);
    }

}
