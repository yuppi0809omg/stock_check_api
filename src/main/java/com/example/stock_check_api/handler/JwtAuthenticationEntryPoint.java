package com.example.stock_check_api.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    // これ何の為？

    @Qualifier("handlerExceptionResolver")
    @Autowired
    private HandlerExceptionResolver resolver;

        @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            resolver.resolveException(request, response, null, exception);
    }

//    @Override
//    public void commence(HttpServletRequest httpServletRequest,
//                         HttpServletResponse httpServletResponse,
//                         AuthenticationException e) throws IOException, ServletException {
//        logger.error(e.getMessage());
//        logger.error("腹たつわ");
//        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ダメよ〜");
//    }
}



