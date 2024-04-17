package com.lifu.wsms.reload.service.user;

import com.lifu.wsms.reload.api.contract.user.RoleService;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.enums.UserPermission;
import com.lifu.wsms.reload.enums.UserRole;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class RoleRecord implements RoleService {
    @Override
    public Either<FailureResponse, SuccessResponse> findAllRoles(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findRolesByUser(String username) {
        return null;
    }

    @Override
    public ApiResponse addPermissionsToRole(UserRole role, Set<UserPermission> permissions) {
        return null;
    }

    @Override
    public ApiResponse removePermissionsFromRole(UserRole role, Set<UserPermission> permissions) {
        return null;
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findPermissionsByRole(UserRole role) {
        return null;
    }
}
