package com.lifu.wsms.reload.service.user;

import com.lifu.wsms.reload.api.ErrorCode;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;

import static com.lifu.wsms.reload.api.AppUtil.BAD_REQUEST_INVALID_PARAMS_CODE;

public class UserApiService {
    private UserApiService(){}

    /**
     * Validates the request for creating a new user.
     *
     * @param createUserRequest The request object containing user creation data.
     * @return Either a FailureResponse indicating validation failure or a Boolean indicating successful validation.
     *         If the createUserRequest is null, returns a FailureResponse with a BAD_REQUEST status code.
     *         If the createUserRequest is valid, returns true.
     */
    public static Either<FailureResponse, Boolean> validateCreateUser(CreateUserRequest createUserRequest) {
        if (createUserRequest == null) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(BAD_REQUEST_INVALID_PARAMS_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(BAD_REQUEST_INVALID_PARAMS_CODE))
                            .build())
                    .build());
        }
        return Either.right(true);
    }
}
