//package com.example.stock_check_api.handler;
//
//import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import com.example.stock_check_api.exception.ForbiddenException;
//import com.example.stock_check_api.exception.NotAuthorizedException;
//
//@Order(HIGHEST_PRECEDENCE)
//@ControllerAdvice(basePackages = "com.example.stock_check_api.controller.user")
//public class OAuthExceptionHandler extends ResponseEntityExceptionHandler {
//
//    private final Logger logger = LoggerFactory.getLogger(OAuthExceptionHandler.class);
//
//    /**
//     * 401エラー
//     **/
//    @ExceptionHandler(NotAuthorizedException.class)
//    public String handleNotAuthorizedException(Exception exc) {
//        logger.warn(exc.getMessage(), exc);
//        return "error/401";
//    }
//
//    /**
//     * 403エラー
//     **/
//    @ExceptionHandler(ForbiddenException.class)
//    public String handleForbiddenException(Exception exc) {
//        logger.warn(exc.getMessage(), exc);
//        return "error/403";
//    }
//
//    /**
//     * 500エラー
//     **/
//    @ExceptionHandler(Exception.class)
//    public String handleAllException(Exception exc) {
//
//        logger.error(exc.getMessage(), exc);
//        return "error/500";
//    }
//
//}