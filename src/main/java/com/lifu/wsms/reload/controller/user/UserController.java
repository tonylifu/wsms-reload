package com.lifu.wsms.reload.controller.user;

import com.lifu.wsms.reload.api.contract.user.UserService;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public static final String USER_PATH = "/api/v1/users";
    public static final String USER_PATH_USERNAME = USER_PATH + "/{username}";

    @PostMapping(USER_PATH)
    public ResponseEntity<?> createStudent(@RequestBody final CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header("location", USER_PATH + "/" +
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
                                .header("location", USER_PATH + "/" +
                                        successResponse.getBody().get("username").asText())
                                .body(successResponse)
                );
    }
}