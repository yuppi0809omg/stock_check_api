package com.example.stock_check_api.exception;

public class ItemNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 8258513411717996223L;

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
