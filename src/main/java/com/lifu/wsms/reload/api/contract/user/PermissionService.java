package com.lifu.wsms.reload.api.contract.user;

import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.enums.UserPermission;
import com.lifu.wsms.reload.enums.UserRole;
import io.vavr.control.Either;

import java.util.Set;

/**
 * Interface for role permissions related operations.
 */
public interface PermissionService {

    /**
     * Adds permissions to the specified user role.
     *
     * @param role        The user role to which permissions will be added.
     * @param permissions The set of permissions to be added to the role.
     * @return An {@code ApiResponse} indicating the result of the operation.
     */
    ApiResponse addPermissionsToRole(UserRole role, Set<UserPermission> permissions);

    /**

     Removes all permissions associated with a specific user role.
     @param role The user role for which permissions are to be removed.
     @return An ApiResponse indicating the success or failure of the operation.
     */
    ApiResponse removeAllPermissions(UserRole role);

    /**
     * Removes all permissions associated with the specified user role.
     *
     * @param role The user role from which permissions will be removed.
     * @param permissions The set of permissions to be removed from the role.
     * @return An {@code ApiResponse} indicating the result of the operation.
     */
    ApiResponse removePermissionsFromRole(UserRole role, Set<UserPermission> permissions);

    /**
     * Retrieves permissions associated with a given user role.
     *
     * @param role The user role for which permissions are to be retrieved.
     * @return Either a FailureResponse if the permissions cannot be found or a SuccessResponse containing the permissions.
     */
    Either<FailureResponse, SuccessResponse> findPermissionsByRole(UserRole role);
}
