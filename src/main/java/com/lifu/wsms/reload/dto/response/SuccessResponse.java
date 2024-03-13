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
@Builder
public class SuccessResponse {
    private final Map<String, Object> body = new HashMap<>();
    private ApiResponse apiResponse;
}
