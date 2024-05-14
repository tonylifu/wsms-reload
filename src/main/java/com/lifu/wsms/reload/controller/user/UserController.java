package com.lifu.wsms.reload.controller.user;

import com.lifu.wsms.reload.api.contract.user.UserService;
import com.lifu.wsms.reload.dto.request.user.*;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public static final String USER_PATH = "/api/v1/users";
    public static final String USER_PATH_USERNAME = USER_PATH + "/{username}";
    public static final String SET_PASSWORD_PATH = "/set-password";
    public static final String USER_PATH_SET_PASSWORD = USER_PATH_USERNAME + SET_PASSWORD_PATH;
    public static final String CHANGE_PASSWORD_PATH = "/change-password";
    public static final String USER_PATH_CHANGE_PASSWORD = USER_PATH_USERNAME + CHANGE_PASSWORD_PATH;
    public static final String CHANGE_STATUS_PATH = "/change-status";
    public static final String USER_PATH_CHANGE_STATUS = USER_PATH_USERNAME + CHANGE_STATUS_PATH;
    public static final String ADD_ROLES_PATH = "/add-roles";
    public static final String USER_PATH_ADD_ROLES = USER_PATH_USERNAME + ADD_ROLES_PATH;
    public static final String REMOVE_ALL_ROLES_PATH = "/remove-all-roles";
    public static final String USER_PATH_REMOVE_ALL_ROLES = USER_PATH_USERNAME + REMOVE_ALL_ROLES_PATH;
    public static final String REMOVE_ROLE_PATH = "/remove-role";
    public static final String USER_PATH_REMOVE_ROLE = USER_PATH_USERNAME + REMOVE_ROLE_PATH;
    public static final String LOCATION = "location";

    @PostMapping(USER_PATH)
    public ResponseEntity<?> createUser(@RequestBody final CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header(LOCATION, USER_PATH + "/" +
                                        successResponse.getBody().get("username").asText())
                                .body(successResponse)
                );
    }

    @GetMapping(USER_PATH_USERNAME)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) {
        return userService.findUser(username)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header(LOCATION, USER_PATH + "/" +
                                        successResponse.getBody().get("username").asText())
                                .body(successResponse)
                );
    }

    @PutMapping(USER_PATH)
    public ResponseEntity<?> updateUser(@RequestBody final UpdateUserRequest updateUserRequest) {
        return userService.updateUser(updateUserRequest)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header(LOCATION, USER_PATH + "/" +
                                        successResponse.getBody().get("username").asText())
                                .body(successResponse)
                );
    }

    @PutMapping(USER_PATH_SET_PASSWORD)
    public ResponseEntity<?> setPassword(@PathVariable("username") String username,
                                         @RequestBody final PasswordRequest passwordSetRequest) {
        var response = userService.setPassword(username, passwordSetRequest.getPassword());
        return ResponseEntity
                .status(response.getHttpStatusCode())
                .header(LOCATION, USER_PATH + "/" + username)
                .body(response);
    }

    @PutMapping(USER_PATH_CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@PathVariable("username") String username,
                                            @RequestBody final ChangePasswordRequest changePasswordRequest) {
        var response = userService.changePassword(username, changePasswordRequest.getCurrentPassword(),
                changePasswordRequest.getNewPassword());
        return ResponseEntity
                .status(response.getHttpStatusCode())
                .header(LOCATION, USER_PATH + "/" + username)
                .body(response);
    }

    @PutMapping(USER_PATH_CHANGE_STATUS)
    public ResponseEntity<?> changeStatus(@PathVariable("username") String username,
                                            @RequestBody final UserStatusUpdateRequest statusUpdateRequest) {
        var response = userService.updateStatus(username, statusUpdateRequest.getStatus());
        return ResponseEntity
                .status(response.getHttpStatusCode())
                .header(LOCATION, USER_PATH + "/" + username)
                .body(response);
    }

    @PutMapping(USER_PATH_ADD_ROLES)
    public ResponseEntity<ApiResponse> addRoles(@PathVariable("username") String username,
                                                @RequestBody final UpdateUserRoles updateUserRoles) {
        var response = userService.addRoles(username, updateUserRoles.getRoles());
        return ResponseEntity
                .status(response.getHttpStatusCode())
                .header(LOCATION, USER_PATH + "/" + username)
                .body(response);
    }
}
