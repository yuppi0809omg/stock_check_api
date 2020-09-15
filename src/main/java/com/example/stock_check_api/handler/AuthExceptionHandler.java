package com.example.stock_check_api.handler;

import com.example.stock_check_api.exception.EmailFailedToBeSent;
import com.example.stock_check_api.exception.InvalidEmailException;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(1)
@RestControllerAdvice(basePackages = "com.example.stock_check_api.controller.authetication")
public class AuthExceptionHandler  {

    private final Logger logger = LoggerFactory.getLogger(AuthExceptionHandler.class);


    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidEmailException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage());
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailFailedToBeSent.class)
    public ResponseEntity<ErrorResponse> handleException(EmailFailedToBeSent exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exc.getMessage());
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleException(NotAuthorizedException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.UNAUTHORIZED.value(), exc.getMessage());
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthenticationException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.UNAUTHORIZED.value(), Translator.toLocale("global.error.auth"));
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }



    // Globalにおいておくと、他のエラーもこれに引っかかってしまう
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleExceptionA(Exception exc) {
//        ErrorResponse error = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "サーバー側でエラーが発生しました");
//        logger.error(exc.getMessage(), exc);
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }


    private ErrorResponse createErrorResponse(int errorCode, String msg) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(errorCode);
        error.setMessage(msg);
        return error;
    }

}