package com.example.stock_check_api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(0)
@Component
public class ApiLogFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(ApiLogFilter.class);


    @Override
    public final void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 処理の開始時間を記録
        long startTime = System.currentTimeMillis();
        // フィルターチェーンの次のフィルターにリクエストとレスポンスを渡す
        filterChain.doFilter(request, response);
        // 処理が戻ってきた時間から処理時間を記録
        long processingTime = System.currentTimeMillis() - startTime;

        // エンドポイントの有無にかかわらず"api"を含むURLへのアクセスはログ出力
        if (request.getRequestURI().matches(".*/api(/.*)*|.*/(signup|authenticate)")) {
            // ログ出力
            logger.info(
                    "{} \"{} {}\" {} {}(ms)", request.getRemoteHost(), request.getMethod(),
                    request.getRequestURI(), response.getStatus(), processingTime);
        }
    }

}

