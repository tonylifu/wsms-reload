package com.lifu.wsms.reload.api.contract.user;

import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.enums.UserRole;

import java.util.Set;

/**
 * Interface for user roles related operations.
 */
public interface RoleService {
    /**
     * Adds roles to a user.
     *
     * @param username The username of the user to add roles to.
     * @param roles    The set of roles to add.
     * @return An {@link ApiResponse} indicating the result of the operation.
     */
    ApiResponse addRoles(String username, Set<UserRole> roles);

    /**
     * Removes all roles from a user.
     *
     * @param username The username of the user to remove roles from.
     * @return An {@link ApiResponse} indicating the result of the operation.
     */
    ApiResponse removeAllRoles(String username);

    /**
     * Removes a specific role from a user.
     *
     * @param username The username of the user from which the role will be removed.
     * @param role     The role to be removed from the user.
     * @return An {@link ApiResponse} indicating the outcome of the operation.
     */
    ApiResponse removeRole(String username, UserRole role);
}
