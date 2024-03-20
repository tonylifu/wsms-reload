package com.lifu.wsms.reload.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.lifu.wsms.reload.api.AppUtil.INVALID_JSON_REQUEST_CODE;
import static com.lifu.wsms.reload.service.ApiService.buildErrorResponse;


@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Invalid json request => {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, INVALID_JSON_REQUEST_CODE).getLeft());
    }
}
