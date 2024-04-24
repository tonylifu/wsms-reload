package com.lifu.wsms.reload.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.contract.user.RoleService;
import com.lifu.wsms.reload.dto.Item;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.dto.response.user.RoleResponse;
import com.lifu.wsms.reload.dto.response.user.RoleResponses;
import com.lifu.wsms.reload.entity.user.Permission;
import com.lifu.wsms.reload.entity.user.Role;
import com.lifu.wsms.reload.entity.user.User;
import com.lifu.wsms.reload.enums.UserPermission;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.mapper.user.RoleToRoleResponseMapper;
import com.lifu.wsms.reload.repository.PermissionRepository;
import com.lifu.wsms.reload.repository.RoleRepository;
import com.lifu.wsms.reload.repository.UserRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lifu.wsms.reload.api.AppUtil.*;
import static com.lifu.wsms.reload.service.ApiService.*;

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

    @Transactional
    @Override
    public ApiResponse addPermissionsToRole(UserRole role, Set<UserPermission> permissions) {
        try {
            return roleRepository.findByName(role.name())
                    .map(userRole -> {
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        Set<Item> permissionItems = getPermissionItems(permissions);
                        Set<Item> newPermissions = permissions.stream().map(r -> new Item(r.name())).collect(Collectors.toSet());
                        Set<Item> combinedSet = new HashSet<>(permissionItems);
                        combinedSet.addAll(userRole.getPermissions());
                        combinedSet.addAll(newPermissions);
                        userRole.setPermissions(combinedSet);
                        userRole.setLastUpdatedAt(timeStamp);
                        userRole.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        Role savedRole = roleRepository.save(userRole);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Override
    public ApiResponse removeAllPermissions(UserRole role) {
        try {
            return roleRepository.findByName(role.name())
                    .map(r -> {
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        Set<Item> permissions = new HashSet<>();
                        r.setPermissions(permissions);
                        r.setLastUpdatedAt(timeStamp);
                        r.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        Role userRole = roleRepository.save(r);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Override
    public ApiResponse removePermissionsFromRole(UserRole role, Set<UserPermission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST_INVALID_PARAMS_CODE).getLeft().getApiResponse();
        }
        try {
            return roleRepository.findByName(role.name())
                    .map(r -> {
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        r.getPermissions().removeIf(p -> checkAnyMatch(p.getName(), permissions));
                        r.setLastUpdatedAt(timeStamp);
                        r.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        Role userRole = roleRepository.save(r);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus,
                                headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findPermissionsByRole(UserRole role) {
        try {
            if (!roleRepository.existsByName(role.name())) {
                log.error("request error, role: {} does not exist", role);
                return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
            }

            RoleResponse roleResponse = roleRepository.findByName(role.name())
                    .map(RoleToRoleResponseMapper.INSTANCE::toRoleResponse)
                    .orElseThrow(() -> new RuntimeException("request failed"));

            return buildSuccessResponse(objectMapper.valueToTree(roleResponse),
                    HttpStatus.OK, TRANSACTION_SUCCESS_CODE);
        } catch (DataAccessException e) {
            log.error("Fetch roles request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        }
    }

    @Transactional
    protected Set<Item> getPermissionItems(Set<UserPermission> permissions) {
        return createPermissionsIfNotAlreadyExisting(permissions.stream()
                .map(role -> new Item(role.name()))
                .collect(Collectors.toSet()));
    }

    private Set<Item> createPermissionsIfNotAlreadyExisting(Set<Item> permissions) {
        Set<Permission> permissionSet = permissions.stream()
                .filter(permission -> !permissionRepository.existsByName(permission.getName()))
                .map(permission -> {
                    long now = convertLocalDateTimeToLong(LocalDateTime.now());
                    String actionBy = AppUtil.getUserFromSecurityContext();
                    Permission newPermission = new Permission();
                    newPermission.setName(permission.getName());
                    newPermission.setCreatedAt(now);
                    newPermission.setLastUpdatedAt(now);
                    newPermission.setActionBy(actionBy);
                    newPermission.setLastActionBy(actionBy);
                    return newPermission;
                })
                .collect(Collectors.toSet());
        if (!permissionSet.isEmpty()) {
            List<Permission> permissionList = permissionRepository.saveAll(permissionSet);
            log.info("{} new Permissions Added: {}", permissionList.size(), permissionList);
        }
        return permissionSet.stream()
                .map(permission -> new Item(permission.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Checks if any element in the set of permissions matches the specified name.
     *
     * @param name        the name to match against the permissions
     * @param permissions the set of permissions to check
     * @return {@code true} if any permission in the set matches the specified name, {@code false} otherwise
     */
    private boolean checkAnyMatch(String name, Set<UserPermission> permissions) {
        return permissions.stream().anyMatch(permission -> permission.name().equals(name));
    }
}
