package com.lifu.wsms.reload.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.UserService;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.dto.response.user.UserResponse;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.Set;
import static com.lifu.wsms.reload.api.AppUtil.*;
import static com.lifu.wsms.reload.api.AppUtil.convertLocalDateToLong;
import static com.lifu.wsms.reload.service.ApiService.buildSuccessResponse;

@RequiredArgsConstructor
@Slf4j
public class UserRecord implements UserService {
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Either<FailureResponse, SuccessResponse> createUser(CreateUserRequest createUserRequest) {
        LocalDate date = LocalDate.now();
        return buildSuccessResponse(objectMapper.valueToTree(UserResponse.builder()
                        .actionBy(getUserFromSecurityContext())
                        .createdAt(convertLocalDateToLong(date))
                        .designation(createUserRequest.getDesignation())
                        .roles(createUserRequest.getRoles())
                        .lastActionBy(getUserFromSecurityContext())
                        .email(createUserRequest.getEmail())
                        .lastModifiedAt(convertLocalDateToLong(date))
                        .mobile(createUserRequest.getMobile())
                        .username(createUserRequest.getUsername())
                        .password(passwordEncoder.encode("password"))
                .build()), HttpStatus.CREATED, TRANSACTION_CREATED_CODE);
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findUser(String username) {
        return null;
    }

    @Override
    public Either<FailureResponse, SuccessResponse> updateUser(UpdateUserRequest updateUserRequest) {
        return null;
    }

    @Override
    public ApiResponse deleteUser(String username) {
        return null;
    }

    @Override
    public ApiResponse setPassword(String username, char[] password) {
        //TODO the entity User to have isPasswordSet field
        return null;
    }

    @Override
    public ApiResponse changePassword(String username, char[] currentPassword, char[] newPassword) {
        //TODO - user entity to have date of last password change
        return null;
    }

    @Override
    public ApiResponse addRoles(String username, Set<UserRole> roles) {
        return null;
    }

    @Override
    public ApiResponse removeAllRoles(String username) {
        return null;
    }

    @Override
    public ApiResponse updateStatus(String username, UserStatus status) {
        return null;
    }
}
