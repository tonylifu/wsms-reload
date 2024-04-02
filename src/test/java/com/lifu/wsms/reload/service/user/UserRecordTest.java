package com.lifu.wsms.reload.service.user;

import com.lifu.wsms.reload.api.contract.user.UserService;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import com.lifu.wsms.reload.util.SudentTestUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRecordTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void createUserFindUpdateSetPasswordChangePasswordAddRemoveRoleAndDelete() {
        //Given
        var createUserRequest = SudentTestUtil.getCreateUserDTO();

        //When - a user is created
        Either<FailureResponse, SuccessResponse> createUserResponse = createUser(createUserRequest);

        //Then
        assertTrue(createUserResponse.isRight());
        assertEquals(HttpStatus.CREATED, createUserResponse.get().getApiResponse().getHttpStatusCode());
        assertEquals(createUserRequest.getUsername().toLowerCase(),
                createUserResponse.get().getBody().get("username").asText().toLowerCase());
        assertEquals(createUserRequest.getEmail(),
                createUserResponse.get().getBody().get("email").asText());
        assertEquals(createUserRequest.getRoles().size(),
                createUserResponse.get().getBody().get("roles").size());

        //when - a user is read from storage
        var username = createUserRequest.getUsername();
        Either<FailureResponse, SuccessResponse> findUserResponse = findUser(username);

        //Then
        assertTrue(findUserResponse.isRight());
        assertEquals(createUserRequest.getUsername().toLowerCase(),
                findUserResponse.get().getBody().get("username").asText().toLowerCase());
        assertEquals(createUserRequest.getEmail(),
                findUserResponse.get().getBody().get("email").asText());
        assertEquals(createUserRequest.getRoles().size(),
                findUserResponse.get().getBody().get("roles").size());

        //Given
        var updateUserRequest = SudentTestUtil.getUpdateUserDTO();
        updateUserRequest.setUsername(username);
        assertEquals(createUserRequest.getUsername(), updateUserRequest.getUsername());
        assertNotEquals(createUserRequest.getMobile(), updateUserRequest.getMobile());
        assertNotEquals(createUserRequest.getEmail(), updateUserRequest.getEmail());

        //When - user is updated
        Either<FailureResponse, SuccessResponse> updateUserResponse = updateUser(updateUserRequest);
        assertTrue(updateUserResponse.isRight());
        assertEquals(createUserRequest.getUsername().toLowerCase(),
                updateUserResponse.get().getBody().get("username").asText().toLowerCase());
        assertEquals(createUserRequest.getEmail(),
                updateUserResponse.get().getBody().get("email").asText());
        assertEquals(createUserRequest.getRoles().size(),
                updateUserResponse.get().getBody().get("roles").size());

        //Given - a password set or password change
        ApiResponse setPasswordResponse = setPassword(username, "password".toCharArray());
        assertEquals(HttpStatus.OK, setPasswordResponse.getHttpStatusCode());
        ApiResponse passwordChangeResponse = changePassword(username, "password".toCharArray(),
                "password@123".toCharArray());
        assertEquals(HttpStatus.OK, passwordChangeResponse.getHttpStatusCode());

        //When - you query the user details, chnages should be reflected
        Either<FailureResponse, SuccessResponse> findUserResponseAfterPasswordUpate = findUser(username);
        assertTrue(findUserResponseAfterPasswordUpate.isRight());
        assertTrue(findUserResponseAfterPasswordUpate.get().getBody().get("passwordSet").asBoolean());

        //When you remove all roles, add some roles and update status
        ApiResponse removeAllRolesResponse = removeAllRoles(username);
        Either<FailureResponse, SuccessResponse> findUserResponseAfterRemovedAllRoles = findUser(username);

        //Then
        assertTrue(findUserResponseAfterRemovedAllRoles.isRight());
        assertEquals(0, findUserResponseAfterRemovedAllRoles.get().getBody().get("roles").size());
        assertEquals(UserStatus.CREATED.getDisplayName(), findUserResponseAfterRemovedAllRoles.get().getBody().get("status").asText());

        //When you add roles and update status
        Set<UserRole> userRoles = Set.of(UserRole.ADMIN, UserRole.BURSAR, UserRole.CASHIER);
        ApiResponse addUserRolesResponse = addRoles(username, userRoles);
        ApiResponse updateUserStatusResponse = updateStatus(username, UserStatus.ACTIVE);

        //Then
        assertEquals(HttpStatus.OK, addUserRolesResponse.getHttpStatusCode());
        assertEquals(HttpStatus.OK, updateUserStatusResponse.getHttpStatusCode());

        //When you query user after adding roles and updating status from created to active
        Either<FailureResponse, SuccessResponse> findUserResponseAddingRolesAndUpdatingStatus = findUser(username);

        //Then
        assertTrue(findUserResponseAddingRolesAndUpdatingStatus.isRight());
        assertEquals(userRoles.size(), findUserResponseAddingRolesAndUpdatingStatus.get().getBody().get("roles").size());
        assertEquals(UserStatus.ACTIVE.getDisplayName(),
                findUserResponseAddingRolesAndUpdatingStatus.get().getBody().get("status").asText());

        //finally when you delete
        ApiResponse deleteUserResponse = deleteUser(username);
        assertEquals(HttpStatus.OK, deleteUserResponse.getHttpStatusCode());
    }

    private Either<FailureResponse, SuccessResponse> createUser(CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    private Either<FailureResponse, SuccessResponse> findUser(String username) {
        return userService.findUser(username);
    }

    private Either<FailureResponse, SuccessResponse> updateUser(UpdateUserRequest updateUserRequest) {
        return userService.updateUser(updateUserRequest);
    }

    private ApiResponse deleteUser(String username) {
        return userService.deleteUser(username);
    }

    private ApiResponse setPassword(String username, char[] password) {
        return userService.setPassword(username, passwordEncoder.encode(new String(password)).toCharArray());
    }

    private ApiResponse changePassword(String username, char[] currentPassword, char[] newPassword) {
        return userService.changePassword(username, currentPassword, newPassword);
    }

    private ApiResponse addRoles(String username, Set<UserRole> roles) {
        return userService.addRoles(username, roles);
    }

    private ApiResponse removeAllRoles(String username) {
        return userService.removeAllRoles(username);
    }

    private ApiResponse updateStatus(String username, UserStatus status) {
        return userService.updateStatus(username, status);
    }
}