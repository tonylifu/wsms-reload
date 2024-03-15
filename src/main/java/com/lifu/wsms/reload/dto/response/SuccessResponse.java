package com.lifu.wsms.reload.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
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
    private JsonNode body;
    private ApiResponse apiResponse;
}
