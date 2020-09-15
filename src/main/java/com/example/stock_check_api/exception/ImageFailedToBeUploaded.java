package com.example.stock_check_api.exception;

public class ImageFailedToBeUploaded extends RuntimeException {
    private static final long serialVersionUID = 4897382739117327701L;

    public ImageFailedToBeUploaded(String message) {
        super(message);
    }

    public ImageFailedToBeUploaded(String message, Throwable cause) {
        super(message, cause);
    }

}
