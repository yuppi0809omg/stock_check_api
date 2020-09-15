package com.example.stock_check_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.stock_check_api.dto.ApiLogSearchResultsForm;
import com.example.stock_check_api.entity.ApiLog;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Integer> {
    @Query(value = "SELECT new com.example.stock_check_api.dto.ApiLogSearchResultsForm(a.requestUrl, a.requestMethod, a.statusCode, SUM(a.accessCount), (SUM(a.avgProcessingTime)/COUNT(a.accessCount)))" +
            "from ApiLog a WHERE a.aggregationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY a.requestUrl, a.statusCode, a.requestMethod ORDER BY a.requestUrl")
    List<ApiLogSearchResultsForm> searchAndAggregateByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}