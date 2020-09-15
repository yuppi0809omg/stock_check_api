package com.example.stock_check_api.exception;

public class ImageNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 4897382739117327701L;

    public ImageNotFoundException(String message) {
        super(message);
    }

    public ImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
