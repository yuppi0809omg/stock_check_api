package com.example.stock_check_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockCheckApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockCheckApiApplication.class, args);
    }

}
