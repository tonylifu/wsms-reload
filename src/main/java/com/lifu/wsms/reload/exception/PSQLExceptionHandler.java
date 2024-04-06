package com.lifu.wsms.reload.exception;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.lifu.wsms.reload.api.AppUtil.PSQL_PERSISTENCE_ERROR_CODE;
import static com.lifu.wsms.reload.service.ApiService.buildErrorResponse;

@RestControllerAdvice
@Slf4j
public class PSQLExceptionHandler {

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<FailureResponse> handlePSQLException(PSQLException ex) {
        var exceptionMessage = ex.getMessage();
        var extractedQuotes = AppUtil.extractQuotedStrings(exceptionMessage);
        log.error("SQL constraints keywords => {}", extractedQuotes);
        log.error("PSQLException occurred: {}", exceptionMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, PSQL_PERSISTENCE_ERROR_CODE).getLeft());
    }
}
