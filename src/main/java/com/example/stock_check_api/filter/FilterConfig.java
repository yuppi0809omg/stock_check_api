package com.example.stock_check_api.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ApiLogFilter> filterRegistrationBean() {
        FilterRegistrationBean<ApiLogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiLogFilter());
        return registrationBean;
    }
}

