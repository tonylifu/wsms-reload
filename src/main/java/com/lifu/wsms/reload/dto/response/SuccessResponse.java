package com.lifu.wsms.reload.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.util.Map;

@Getter
@Setter
public class SuccessResponse extends ApiResponse {
    Map<String, Object> body;

    public SuccessResponse(String responseMessage,
                           String responseCode,
                           HttpStatus httpStatusCode,
                           boolean isError,
                           Map<String, Object> body) {
        super(responseMessage, responseCode, httpStatusCode, isError);
        this.body = body;
    }
}
