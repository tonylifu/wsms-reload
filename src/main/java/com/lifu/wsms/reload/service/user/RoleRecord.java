package com.lifu.wsms.reload.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifu.wsms.reload.api.contract.user.RoleService;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.dto.response.student.StudentResponse;
import com.lifu.wsms.reload.dto.response.student.StudentResponses;
import com.lifu.wsms.reload.dto.response.user.RoleResponse;
import com.lifu.wsms.reload.dto.response.user.RoleResponses;
import com.lifu.wsms.reload.entity.user.Role;
import com.lifu.wsms.reload.enums.UserPermission;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.mapper.student.StudentToStudentResponseMapper;
import com.lifu.wsms.reload.mapper.user.RoleToRoleResponseMapper;
import com.lifu.wsms.reload.repository.PermissionRepository;
import com.lifu.wsms.reload.repository.RoleRepository;
import com.lifu.wsms.reload.repository.UserRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lifu.wsms.reload.api.AppUtil.*;
import static com.lifu.wsms.reload.service.ApiService.buildErrorResponse;
import static com.lifu.wsms.reload.service.ApiService.buildSuccessResponse;

@Slf4j
@RequiredArgsConstructor
public class RoleRecord implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    public Either<FailureResponse, SuccessResponse> findAllRoles(int pageNumber, int pageSize) {
        try {
            List<RoleResponse> roleResponses = roleRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream()
                    .map(RoleToRoleResponseMapper.INSTANCE::toRoleResponse)
                    .toList();
            return buildSuccessResponse(objectMapper.valueToTree(
                            RoleResponses.builder()
                                    .roles(new HashSet<>(roleResponses))
                                    .build()),
                    HttpStatus.OK, TRANSACTION_SUCCESS_CODE);
        } catch (DataAccessException e) {
            log.error("Fetch roles request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        }
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findRolesByUser(String username) {
        if (username == null || username.isEmpty()) {
            log.error("error, missing username: {}", username);
            return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_USER_NAME_CODE);
        }
        username = username.strip().toUpperCase();
        try {
            if (!userRepository.existsByUsername(username)) {
                log.error("request error, username: {} does not exist", username);
                return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
            }

            Set<RoleResponse> roleResponses = userRepository.findByUsername(username)
                    .map(user -> {
                        return user.getRoles().stream()
                                .map(role -> roleRepository.findByName(role.getName()))
                                .map(Optional::orElseThrow)
                                .map(RoleToRoleResponseMapper.INSTANCE::toRoleResponse)
                                .collect(Collectors.toSet());
                    })
                    .orElseThrow(() -> new RuntimeException("request failed"));

            return buildSuccessResponse(objectMapper.valueToTree(
                            RoleResponses.builder()
                                    .roles(roleResponses)
                                    .build()),
                    HttpStatus.OK, TRANSACTION_SUCCESS_CODE);
        } catch (DataAccessException e) {
            log.error("Fetch roles request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        }
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
