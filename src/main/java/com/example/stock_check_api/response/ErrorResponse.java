package com.example.stock_check_api.response;

import java.util.List;
import java.util.Map;

public class ErrorResponse extends ApiResponse {
    private int status;
    private Map<String, List<String>> errors;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(int status, String message, Map<String, List<String>> errors) {
        super(message);
        this.status = status;
        this.errors = errors;
    }


    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



}
