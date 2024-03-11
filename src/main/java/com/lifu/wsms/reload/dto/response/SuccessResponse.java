package com.lifu.wsms.reload.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SuccessResponse extends ApiResponse {

    private final Map<String, Object> body;

    @Builder
    public SuccessResponse(String responseMessage,
                           String responseCode,
                           HttpStatus httpStatusCode,
                           boolean isError,
                           Map<String, Object> body) {
        super(responseMessage, responseCode, httpStatusCode, isError);
        this.body = Map.copyOf(body);
    }
}
