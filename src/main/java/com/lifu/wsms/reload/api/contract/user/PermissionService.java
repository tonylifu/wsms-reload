package com.lifu.wsms.reload.api.contract.user;

import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.enums.UserPermission;
import com.lifu.wsms.reload.enums.UserRole;

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
    ApiResponse addPermissions(UserRole role, Set<UserPermission> permissions);

    /**
     * Removes all permissions associated with the specified user role.
     *
     * @param role The user role from which permissions will be removed.
     * @param permissions The set of permissions to be removed from the role.
     * @return An {@code ApiResponse} indicating the result of the operation.
     */
    ApiResponse removePermissions(UserRole role, Set<UserPermission> permissions);
}
