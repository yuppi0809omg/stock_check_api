package com.example.stock_check_api.dto;

public class ApiLogSearchResultsForm {
    private String requestUrl;

    private String requestMethod;

    private int statusCode;

    private long accessCount;

    private long avgProcessingTime;

    public ApiLogSearchResultsForm(String requestUrl, String requestMethod, int statusCode, long accessCount, long avgProcessingTime) {
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

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

    public long getAvgProcessingTime() {
        return avgProcessingTime;
    }

    public void setAvgProcessingTime(long avgProcessingTime) {
        this.avgProcessingTime = avgProcessingTime;
    }
}

