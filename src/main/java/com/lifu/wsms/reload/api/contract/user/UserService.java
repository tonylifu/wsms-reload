package com.lifu.wsms.reload.api.contract.user;

import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import io.vavr.control.Either;

import java.util.Set;

/**
 * Interface for user-related operations.
 */
public interface UserService extends RoleService {

    /**
     * Creates a new user.
     *
     * @param createUserRequest The request object containing user details.
     * @return Either a {@link FailureResponse} if the operation fails, or a {@link SuccessResponse} if successful.
     */
    Either<FailureResponse, SuccessResponse> createUser(CreateUserRequest createUserRequest);

    /**
     * Finds a user by username.
     *
     * @param username The username of the user to find.
     * @return Either a {@link FailureResponse} if the user is not found, or a {@link SuccessResponse} with user details.
     */
    Either<FailureResponse, SuccessResponse> findUser(String username);

    /**
     * Updates an existing user.
     *
     * @param updateUserRequest The request object containing updated user details.
     * @return Either a {@link FailureResponse} if the operation fails, or a {@link SuccessResponse} if successful.
     */
    Either<FailureResponse, SuccessResponse> updateUser(UpdateUserRequest updateUserRequest);

    /**
     * Deletes a user by username.
     *
     * @param username The username of the user to delete.
     * @return An {@link ApiResponse} indicating the result of the operation.
     */
    ApiResponse deleteUser(String username);

    /**
     * Sets the password for a user.
     *
     * @param username The username of the user whose password will be set.
     * @param password The new password to set.
     * @return An {@link ApiResponse} indicating the result of the operation.
     */
    ApiResponse setPassword(String username, char[] password);

    /**
     * Changes the password for a user.
     *
     * @param username       The username of the user whose password will be changed.
     * @param currentPassword The current password of the user.
     * @param newPassword     The new password to set.
     * @return An {@link ApiResponse} indicating the result of the operation.
     */
    ApiResponse changePassword(String username, char[] currentPassword, char[] newPassword);

    /**
     * Updates the status of a user.
     *
     * @param username The username of the user whose status will be updated.
     * @param status   The new status to set.
     * @return An {@link ApiResponse} indicating the result of the operation.
     */
    ApiResponse updateStatus(String username, UserStatus status);

}

