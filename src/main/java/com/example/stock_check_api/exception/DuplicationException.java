package com.example.stock_check_api.exception;

public class DuplicationException extends RuntimeException{

    private String type;

    public String getType() {
        return type;
    }

    public DuplicationException(String message, String type) {
        super(message);
        this.type = type;
    }

    public DuplicationException(String message) {
        super(message);

    }

}
