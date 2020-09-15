package com.example.stock_check_api.exception;

public class ItemDuplicationException extends DuplicationException {
    private static final long serialVersionUID = 4897382739117327701L;

    public ItemDuplicationException(String message) {
        super(message);
    }

    public ItemDuplicationException(String message, String type) {
        super(message, type);
    }

}
