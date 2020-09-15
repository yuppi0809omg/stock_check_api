package com.example.stock_check_api.handler;

import com.example.stock_check_api.exception.DuplicationException;
import com.example.stock_check_api.exception.NotFoundException;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse error = createErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getParameterName() + Translator.toLocale("global.error.missing.params"));
        logger.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = createErrorResponse(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("global.error.invalid.params"));
        logger.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = createErrorResponse(HttpStatus.NOT_FOUND.value(), Translator.toLocale("global.error.page_not_found"));

        return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }



    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exc,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ErrorResponse error = createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), Translator.toLocale("global.error.method_not_allowed"));
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exc,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

//        Map<String, Object> body = new LinkedHashMap<>();

        Map<String, List<String>> errors = new LinkedHashMap<>();

                exc.getBindingResult()
                .getFieldErrors()
                .stream().forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    if(errors.containsKey(fieldName)){
                        errors.get(fieldName).add(fieldError.getDefaultMessage());

                    }else {
                        List<String> list = new ArrayList<String>();
                        list.add(fieldError.getDefaultMessage());

                        errors.put(fieldName, list);
                    }
                });


//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());

        ErrorResponse error = createValidationErrorResponse(status.value(), errors);

//        body.put("status", status.value());
//        body.put("message", errors);
//        body.put("timestamp", LocalDateTime.now());
        logger.warn(exc.getMessage(), exc);

        return new ResponseEntity<>(error, headers, status);

    }

    // Spring Securityの認証通らなかった場合に投げられるエラーAuthentication Exceptionを処理
    // AuthExceptionHandlerに置いておくと、EntryPointで処理されるexceptionを拾わないのでこちらへ移動
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthenticationException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.UNAUTHORIZED.value(), Translator.toLocale("global.error.auth"));
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(DuplicationException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage());
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.FORBIDDEN.value(), Translator.toLocale("global.error.method_access_denied"));
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage());
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleException(MaxUploadSizeExceededException exc) {
        ErrorResponse error = createErrorResponse(HttpStatus.BAD_REQUEST.value(), Translator.toLocale("image.upload.error.size"));
        logger.warn(exc.getMessage(), exc);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleExceptionA(Exception exc) {
//        ErrorResponse error = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), Translator.toLocale("global.error.internal"));
//        logger.error(exc.getMessage(), exc);
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }


    private ErrorResponse createErrorResponse(int errorCode, String msg) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(errorCode);
        error.setMessage(msg);
        return error;
    }

    // バリデーションエラー用
    private ErrorResponse createValidationErrorResponse(int errorCode, Map<String, List<String>>errors) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(errorCode);
        error.setMessage(Translator.toLocale("global.error.validation"));
        error.setErrors(errors);
        return error;
    }

}
