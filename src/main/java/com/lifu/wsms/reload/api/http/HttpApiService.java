package com.lifu.wsms.reload.api.http;

import com.lifu.wsms.reload.dto.response.HttpApiResponse;

/**
 * This API makes http request
 */
public interface HttpApiService {

    HttpApiResponse postApiRequest(String apiRequest, String endPoint, String token);
    HttpApiResponse getApiRequest(String endPoint, String token);
}
