package com.example.stock_check_api.response;

import java.time.LocalDateTime;

public class ApiResponse {
    private String message;
    private LocalDateTime timeStamp;

    public ApiResponse() {
        this(null);
    }

    public ApiResponse(String message) {
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

}

