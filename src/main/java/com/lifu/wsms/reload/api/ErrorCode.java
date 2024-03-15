package com.lifu.wsms.reload.api;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.lifu.wsms.reload.api.AppUtil.*;

@Getter
public enum ErrorCode {
    INVALID_REQUEST(BAD_REQUEST_CODE, "Invalid request"),
    UNAUTHENTICATED_REQUEST(FAILED_AUTHENTICATION_CODE, "Failed authentication request"),
    UNAUTHORIZED_REQUEST(FAILED_AUTHORIZATION_CODE, "Failed authorization request"),
    NOT_FOUND(RESOURCE_NOT_FOUND_CODE, "Resource was not found"),
    //Application specifics
    INVALID_STUDENT_ID(INVALID_STUDENT_ID_CODE, "Invalid student Id"),
    INVALID_STUDENT_NAME(MISSING_NAMES_CODE, "Invalid student name"),
    INVALID_DOB(INVALID_DOB_CODE, "Invalid date of birth"),
    // Add more error codes and messages as needed
    DATA_PERSISTENCE_ERROR(DATA_PERSISTENCE_ERROR_CODE, "Failed data persistence"),

    // Dummy entry to avoid null pointer exception
    UNKNOWN_ERROR(UNKNOWN_ERROR_CODE, "Unknown error");

    private static final Map<String, String> codeToMessageMap = new HashMap<>();

    static {
        for (ErrorCode errorCode : ErrorCode.values()) {
            codeToMessageMap.put(errorCode.code, errorCode.message);
        }
    }

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(String code) {
        return codeToMessageMap.getOrDefault(code, UNKNOWN_ERROR.message);
    }
}