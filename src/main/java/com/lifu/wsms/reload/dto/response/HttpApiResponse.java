package com.lifu.wsms.reload.dto.response;

import lombok.Builder;

import java.net.http.HttpHeaders;

@Builder
public record HttpApiResponse(String responseBody, int statusCode, HttpHeaders headers) { }
