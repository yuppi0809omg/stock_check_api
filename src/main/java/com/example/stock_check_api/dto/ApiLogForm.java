package com.example.stock_check_api.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

public class ApiLogForm {

    @NotNull(message = "開始日の日付を選択して下さい")
    @PastOrPresent(message = "開始日に過去か現在の日付を入力して下さい")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "終了日の日付を選択して下さい")
    @PastOrPresent(message = "終了日に過去か現在の日付を入力して下さい")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public ApiLogForm() {
    }

    public ApiLogForm(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
