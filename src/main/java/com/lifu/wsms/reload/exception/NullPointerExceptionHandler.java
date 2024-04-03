package com.lifu.wsms.reload.exception;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.lifu.wsms.reload.api.AppUtil.BAD_REQUEST_INVALID_PARAMS_CODE;
import static com.lifu.wsms.reload.service.student.StudentApiService.buildErrorResponse;

@RestControllerAdvice
@Slf4j
public class NullPointerExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<FailureResponse> handleNullPointerException(NullPointerException ex) {
        var exceptionMessage = ex.getMessage();
        var extractedQuotes = AppUtil.extractQuotedStrings(exceptionMessage);
        log.error("NullPointer error keywords => {}", extractedQuotes);
        log.error("NullPointer occurred: {}", exceptionMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST_INVALID_PARAMS_CODE).getLeft());
    }
}