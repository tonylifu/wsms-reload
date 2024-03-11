package com.lifu.wsms.reload.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class FailureResponse extends ApiResponse {
    public FailureResponse(String responseMessage, String responseCode, HttpStatus httpStatusCode, boolean isError) {
        super(responseMessage, responseCode, httpStatusCode, isError);
    }
}
