package com.example.stock_check_api.dto;

public class ApiLogDto {
    private String requestUrl;

    private String requestMethod;

    private int statusCode;

    private int accessCount;

    private int avgProcessingTime;


    public ApiLogDto(String requestUrl, String requestMethod, int statusCode, int accessCount, int avgProcessingTime) {
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.statusCode = statusCode;
        this.accessCount = accessCount;
        this.avgProcessingTime = avgProcessingTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }

    public int getAvgProcessingTime() {
        return avgProcessingTime;
    }

    public void setAvgProcessingTime(int avgProcessingTime) {
        this.avgProcessingTime = avgProcessingTime;
    }
}

