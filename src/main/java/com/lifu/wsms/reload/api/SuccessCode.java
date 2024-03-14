package com.lifu.wsms.reload.api;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.lifu.wsms.reload.api.AppUtil.*;

@Getter
public enum SuccessCode {
    TRANSACTION_CREATED(TRANSACTION_CREATED_CODE, "Insertion was successful"),
    TRANSACTION_OKAY(TRANSACTION_OKAY_CODE, "Transaction okay"),

    SUCCESS(TRANSACTION_SUCCESS_CODE, "Successful");

    private static final Map<String, String> codeToMessageMap = new HashMap<>();

    static {
        for (SuccessCode successCode : SuccessCode.values()) {
            codeToMessageMap.put(successCode.code, successCode.message);
        }
    }

    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(String code) {
        return codeToMessageMap.getOrDefault(code, SUCCESS.message);
    }
}
