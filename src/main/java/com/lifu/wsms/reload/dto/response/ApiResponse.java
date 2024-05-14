package com.lifu.wsms.reload.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private String responseMessage;
    private String responseCode;
    private HttpStatus httpStatusCode;
    private HttpHeaders httpHeaders;
    private boolean isError;
}
