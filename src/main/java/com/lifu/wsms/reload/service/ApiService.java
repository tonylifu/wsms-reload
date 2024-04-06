package com.lifu.wsms.reload.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lifu.wsms.reload.api.ErrorCode;
import com.lifu.wsms.reload.api.SuccessCode;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import io.vavr.control.Either;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.lifu.wsms.reload.api.AppUtil.TRANSACTION_SUCCESS_CODE;

public class ApiService {
    private ApiService() {}

    public static ApiResponse buildSuccessApiResponse(HttpStatus httpStatus, HttpHeaders headers, String transactionCode) {
        return ApiResponse.builder()
                .isError(false)
                .httpStatusCode(httpStatus)
                .httpHeaders(headers)
                .responseCode(transactionCode)
                .responseMessage(SuccessCode.getMessageByCode(transactionCode))
                .build();
    }

    /**
     * Builds a FailureResponse containing error information based on the provided HTTP status code and response code.
     *
     * @param httpStatus   The HTTP status code indicating the status of the response.
     * @param responseCode The error response code indicating the type of error.
     * @return An Either monad containing a FailureResponse if an error occurred, or a SuccessResponse if no error occurred.
     *         The FailureResponse contains details about the error, including the HTTP status code, response code, and message.
     */
    public static Either<FailureResponse, SuccessResponse> buildErrorResponse(HttpStatus httpStatus,
                                                                              String responseCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-HttpStatus", httpStatus.toString());
        return Either.left(FailureResponse.builder()
                .apiResponse(ApiResponse.builder()
                        .isError(true)
                        .httpStatusCode(httpStatus)
                        .responseCode(responseCode)
                        .responseMessage(ErrorCode.getMessageByCode(responseCode))
                        .httpHeaders(headers)
                        .build())
                .build());
    }


    /**
     * Constructs a SuccessResponse containing the provided body, HTTP status code, and response code.
     *
     * @param body         The JSON body to include in the success response.
     * @param httpStatus   The HTTP status code indicating the success status of the response.
     * @param responseCode The response code indicating the type of success.
     * @return Either a SuccessResponse containing the provided body and response details if successful,
     *         or a FailureResponse if an error occurs during the process.
     */
    public static Either<FailureResponse, SuccessResponse> buildSuccessResponse(JsonNode body, HttpStatus httpStatus,
                                                                                String responseCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-HttpStatus", httpStatus.toString());
        return Either.right(SuccessResponse.builder()
                .body(body)
                .apiResponse(ApiResponse.builder()
                        .httpStatusCode(httpStatus)
                        .responseCode(responseCode)
                        .responseMessage(SuccessCode.getMessageByCode(responseCode))
                        .httpHeaders(headers)
                        .isError(false)
                        .build())
                .build());
    }
}
