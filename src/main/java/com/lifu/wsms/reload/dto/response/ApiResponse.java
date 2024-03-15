package com.lifu.wsms.reload.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ApiResponse {
    private String responseMessage;
    private String responseCode;
    private HttpStatus httpStatusCode;
    private boolean isError;
}
