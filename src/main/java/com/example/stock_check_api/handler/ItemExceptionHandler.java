package com.example.stock_check_api.handler;

import com.example.stock_check_api.exception.*;
import com.example.stock_check_api.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@Order(1)
@RestControllerAdvice(basePackages = "com.example.stock_check_api.controller.item")
public class ItemExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ItemExceptionHandler.class);


    @ExceptionHandler(ImageFailedToBeUploaded.class)
    public ResponseEntity<ErrorResponse> handleException(ImageFailedToBeUploaded exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exc.getMessage());
        logger.error(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleException(MultipartException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage());
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse>handleNotAuthorizedException(ForbiddenException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.FORBIDDEN.value(), exc.getMessage());
        logger.error(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }


    private ErrorResponse createErrorResponse(int errorCode, String msg) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(errorCode);
        error.setMessage(msg);
        return error;
    }

}
