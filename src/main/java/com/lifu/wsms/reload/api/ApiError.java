package com.lifu.wsms.reload.api;

import java.util.HashMap;
import java.util.Map;

public enum ApiError {
    // Define enum constants with error codes and corresponding error messages
    INVALID_INPUT(400, "Invalid input provided"),
    UNAUTHORIZED(401, "Unauthorized access"),
    FORBIDDEN(403, "Access forbidden"),
    NOT_FOUND(404, "Resource not found"),
    SERVER_ERROR(500, "Internal server error");

    private final int code;
    private final String message;

    // Initialize map to store error messages by error code
    private static final Map<Integer, ApiError> ERROR_BY_CODE = new HashMap<>();

    // Static block to populate the map with error codes and messages
    static {
        for (ApiError error : ApiError.values()) {
            ERROR_BY_CODE.put(error.getCode(), error);
        }
    }

    ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // Method to retrieve error object by error code
    public static ApiError getByCode(int code) {
        return ERROR_BY_CODE.getOrDefault(code, SERVER_ERROR);
    }
}
