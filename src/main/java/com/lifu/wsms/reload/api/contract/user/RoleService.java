package com.lifu.wsms.reload.api.contract.user;

import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import io.vavr.control.Either;

/**
 * Service interface for managing roles.
 */
public interface RoleService extends PermissionService {

    /**
     * Retrieves a paginated list of all roles.
     *
     * @param pageNumber The page number (starting from 1) of the results to retrieve.
     * @param pageSize   The maximum number of roles per page.
     * @return An {@link Either} representing either a {@link FailureResponse} if the operation fails
     *         or a {@link SuccessResponse} containing the paginated list of roles.
     */
    Either<FailureResponse, SuccessResponse> findAllRoles(int pageNumber, int pageSize);

    /**
     * Retrieves roles associated with a specific user.
     *
     * @param username The username of the user whose roles are to be retrieved.
     * @return An {@link Either} representing either a {@link FailureResponse} if the operation fails
     *         or a {@link SuccessResponse} containing the roles associated with the user.
     */
    Either<FailureResponse, SuccessResponse> findRolesByUser(String username);
}

