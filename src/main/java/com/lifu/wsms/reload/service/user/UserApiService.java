package com.lifu.wsms.reload.service.user;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.ErrorCode;
import com.lifu.wsms.reload.dto.Item;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.entity.user.Role;
import com.lifu.wsms.reload.entity.user.User;
import com.lifu.wsms.reload.enums.UserRole;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.lifu.wsms.reload.api.AppUtil.*;
import static com.lifu.wsms.reload.api.AppUtil.INVALID_DOB_CODE;

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

        if (createUserRequest.getUsername() == null || createUserRequest.getUsername().isEmpty()) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(MISSING_USER_NAME_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(MISSING_USER_NAME_CODE))
                            .build())
                    .build());
        }

        if (createUserRequest.getFirstName() == null || createUserRequest.getFirstName().isEmpty()
                || createUserRequest.getLastName() == null || createUserRequest.getLastName().isEmpty()) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(MISSING_NAMES_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(MISSING_NAMES_CODE))
                            .build())
                    .build());
        }

        if (!(AppUtil.isValidLocalDateString(createUserRequest.getDob()))
                || !(AppUtil.isParseableLocalDateString(createUserRequest.getDob()))) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(INVALID_DOB_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(INVALID_DOB_CODE))
                            .build())
                    .build());
        }
        return Either.right(true);
    }

    public static Either<FailureResponse, Boolean> validateUpdateUser(UpdateUserRequest updateUserRequest) {
        if (updateUserRequest == null) {
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

    /**
     * Populates a {@link User} entity for an update operation based on the provided {@link UpdateUserRequest}.
     * Transfers non-null fields from the {@code updateUserRequest} to the {@code user} entity.
     * If a field in the {@code updateUserRequest} is null, the corresponding field in the {@code user} entity remains unchanged.
     * Converts the {@link UserRole} set from the {@code updateUserRequest} to {@link Role} entities and sets them to the user roles.
     *
     * @param user               the {@link User} entity to be updated
     * @param updateUserRequest the {@link UpdateUserRequest} containing the updated user details
     * @return the updated {@link User} entity with transferred fields from the {@code updateUserRequest}
     * @see User
     * @see UpdateUserRequest
     * @see UserRole
     * @see Role
     */
    public static User populateUserForUpdate(User user, UpdateUserRequest updateUserRequest) {
        // Transfer fields from updateUserRequest to user entity
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        if (updateUserRequest.getUsername() != null) {
            user.setUsername(updateUserRequest.getUsername());
        }
        if (updateUserRequest.getDesignation() != null) {
            user.setDesignation(updateUserRequest.getDesignation());
        }
        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getMobile() != null) {
            user.setMobile(updateUserRequest.getMobile());
        }
        if (updateUserRequest.getRoles() != null) {
            // Convert UserRole set to Role entities and set it to user roles
            Set<Item> roles = new HashSet<>();
            for (UserRole userRole : updateUserRequest.getRoles()) {
                Item role = new Item();
                role.setName(userRole.name());
                roles.add(role);
            }
            user.setRoles(roles);
        }
        if (updateUserRequest.getStatus() != null) {
            user.setStatus(updateUserRequest.getStatus());
        }

        if (updateUserRequest.getGender() != null) {
            user.setGender(updateUserRequest.getGender());
        }

        user.setLastActionBy(AppUtil.getUserFromSecurityContext());

        LocalDateTime now = LocalDateTime.now();
        user.setLastUpdatedAt(AppUtil.convertLocalDateTimeToLong(now));

        return user;
    }
}
