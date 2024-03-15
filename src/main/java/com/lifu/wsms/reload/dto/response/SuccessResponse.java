package com.lifu.wsms.reload.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuccessResponse {
    private JsonNode body;
    private ApiResponse apiResponse;
}
