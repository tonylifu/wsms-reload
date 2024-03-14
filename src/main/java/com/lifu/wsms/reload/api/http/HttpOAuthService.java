package com.lifu.wsms.reload.api.http;

import com.lifu.wsms.reload.dto.response.HttpApiResponse;

/**
 * This API makes http request
 */
public interface HttpOAuthService {
    /**
     * @param request
     * @return
     */
    HttpApiResponse postRequest(String request, String endPoint);
}
