package com.example.stock_check_api.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "api_log")
public class ApiLog {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "request_url", nullable = false)
    private String requestUrl;

    @Column(name = "request_method", nullable = false)
    private String requestMethod;

    @Column(name = "status_code", nullable = false)
    private int statusCode;

    @Column(name = "access_count", nullable = false)
    private int accessCount;

    @Column(name = "avg_processing_time", nullable = false)
    private int avgProcessingTime;

    @Column(name = "aggregation_date", nullable = false)
    private LocalDate aggregationDate;

    public ApiLog() {
    }

    public ApiLog(String requestUrl, String requestMethod, int statusCode, int accessCount, int avgProcessingTime) {
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.statusCode = statusCode;
        this.accessCount = accessCount;
        this.avgProcessingTime = avgProcessingTime;
        aggregationDate = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getAggregationDate() {
        return aggregationDate;
    }

    public void setAggregationDate(LocalDate aggregationDate) {
        this.aggregationDate = aggregationDate;
    }
}
