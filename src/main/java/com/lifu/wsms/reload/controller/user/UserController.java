package com.lifu.wsms.reload.controller.user;

import com.lifu.wsms.reload.api.contract.user.UserService;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.PasswordSetRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public static final String USER_PATH = "/api/v1/users";
    public static final String USER_PATH_USERNAME = USER_PATH + "/{username}";
    public static final String USER_PATH_SET_PASSWORD = USER_PATH_USERNAME + "/set-password";
    public static final String USER_PATH_CHANGE_PASSWORD = USER_PATH_USERNAME + "/change-password";
    private static final String LOCATION = "location";

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
    public ResponseEntity<?> setPassword(@PathVariable("username") String username, @RequestBody final PasswordSetRequest passwordSetRequest) {
        var response = userService.setPassword(username, passwordSetRequest.getPassword());
        return ResponseEntity
                .status(response.getHttpStatusCode())
                .header(LOCATION, USER_PATH + "/" + username)
                .body(response);
    }
}
