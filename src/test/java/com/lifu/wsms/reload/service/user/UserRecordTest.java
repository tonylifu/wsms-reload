package com.lifu.wsms.reload.service.user;

import com.lifu.wsms.reload.api.ErrorCode;
import com.lifu.wsms.reload.api.contract.user.UserService;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import com.lifu.wsms.reload.repository.RoleRepository;
import com.lifu.wsms.reload.repository.UserRepository;
import com.lifu.wsms.reload.util.SudentTestUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.lifu.wsms.reload.api.AppUtil.BAD_REQUEST_INVALID_PARAMS_CODE;
import static com.lifu.wsms.reload.util.UserTestUtil.getCreateUserDTO;
import static com.lifu.wsms.reload.util.UserTestUtil.getUpdateUserDTO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRecordTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void createUserFindUpdateSetPasswordChangePasswordAddRemoveRoleAndDelete() {
        //Given
        var createUserRequest = getCreateUserDTO();

        //When - a user is created
        Either<FailureResponse, SuccessResponse> createUserResponse = createUser(createUserRequest);

        //Then
        assertTrue(createUserResponse.isRight());
        assertEquals(HttpStatus.CREATED, createUserResponse.get().getApiResponse().getHttpStatusCode());
        assertEquals(createUserRequest.getUsername().toLowerCase(),
                createUserResponse.get().getBody().get("username").asText().toLowerCase());
        assertEquals(createUserRequest.getEmail(),
                createUserResponse.get().getBody().get("email").asText());

        //when - a user is read from storage
        var username = createUserRequest.getUsername();
        Either<FailureResponse, SuccessResponse> findUserResponse = findUser(username);

        //Then
        assertTrue(findUserResponse.isRight());
        assertEquals(createUserRequest.getUsername().toLowerCase(),
                findUserResponse.get().getBody().get("username").asText().toLowerCase());
        assertEquals(createUserRequest.getEmail(),
                findUserResponse.get().getBody().get("email").asText());

        //Given
        var updateUserRequest = getUpdateUserDTO();
        updateUserRequest.setUsername(username);
        assertEquals(createUserRequest.getUsername(), updateUserRequest.getUsername());
        assertNotEquals(createUserRequest.getMobile(), updateUserRequest.getMobile());
        assertNotEquals(createUserRequest.getEmail(), updateUserRequest.getEmail());

        //When - user is updated
        Either<FailureResponse, SuccessResponse> updateUserResponse = updateUser(updateUserRequest);
        assertTrue(updateUserResponse.isRight());
        assertEquals(createUserRequest.getUsername().toLowerCase(),
                updateUserResponse.get().getBody().get("username").asText().toLowerCase());
        assertEquals(updateUserRequest.getEmail(),
                updateUserResponse.get().getBody().get("email").asText());

        //Given - a password set or password change
        String currentSetPassword = "Password@123";
        String newReplacementPassword = "Test@123";
        ApiResponse setPasswordResponse = setPassword(username, currentSetPassword.toCharArray());
        assertEquals(HttpStatus.NO_CONTENT, setPasswordResponse.getHttpStatusCode());

        Either<FailureResponse, SuccessResponse> findUserResponseAfterPasswordSet = findUser(username);
        String encodedSetPasswordFromStore = findUserResponseAfterPasswordSet.get().getBody().get("password").asText();
        assertTrue(passwordEncoder.matches(currentSetPassword, encodedSetPasswordFromStore));

        ApiResponse passwordChangeResponse = changePassword(username, currentSetPassword.toCharArray(),
                newReplacementPassword.toCharArray());
        assertEquals(HttpStatus.NO_CONTENT, passwordChangeResponse.getHttpStatusCode());

        //When - you query the user details, chnages should be reflected
        Either<FailureResponse, SuccessResponse> findUserResponseAfterPasswordUpate = findUser(username);
        assertTrue(findUserResponseAfterPasswordUpate.isRight());
        assertTrue(findUserResponseAfterPasswordUpate.get().getBody().get("passwordSet").asBoolean());
        String encodedChangedPasswordFromStore = findUserResponseAfterPasswordUpate.get().getBody().get("password").asText();
        assertTrue(passwordEncoder.matches(newReplacementPassword, encodedChangedPasswordFromStore));

        //When you add some roles
        Set<UserRole> someRoles = Set.of(UserRole.ADMIN, UserRole.BURSAR, UserRole.CASHIER);
        ApiResponse addSomeRoles = addRoles(username, someRoles);
        assertEquals(HttpStatus.NO_CONTENT, addSomeRoles.getHttpStatusCode());

        Either<FailureResponse, SuccessResponse> findUserResponseAfterRemovedAllRoles = findUser(username);

        //Then
        assertTrue(findUserResponseAfterRemovedAllRoles.isRight());
        assertEquals(someRoles.size(), findUserResponseAfterRemovedAllRoles.get().getBody().get("roles").size());
        assertEquals(UserStatus.CREATED.getDisplayName().toLowerCase(), findUserResponseAfterRemovedAllRoles.get().getBody().get("status").asText().toLowerCase());

        //When you add roles and update status
        Set<UserRole> userRoles = Set.of(UserRole.SECURITY, UserRole.ADMIN, UserRole.BURSAR, UserRole.CASHIER);
        ApiResponse addUserRolesResponse = addRoles(username, userRoles);
        ApiResponse updateUserStatusResponse = updateStatus(username, UserStatus.ACTIVE);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, addUserRolesResponse.getHttpStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, updateUserStatusResponse.getHttpStatusCode());

        //When you query user after adding roles and updating status from created to active
        Either<FailureResponse, SuccessResponse> findUserResponseAddingRolesAndUpdatingStatus = findUser(username);

        //Then
        assertTrue(findUserResponseAddingRolesAndUpdatingStatus.isRight());
        assertEquals(userRoles.size(), findUserResponseAddingRolesAndUpdatingStatus.get().getBody().get("roles").size());
        assertEquals(UserStatus.ACTIVE.name(),
                findUserResponseAddingRolesAndUpdatingStatus.get().getBody().get("status").asText());

        //When you remove a single role
        ApiResponse removeARoleResponse = removeARole(username, UserRole.ADMIN);
        assertEquals(HttpStatus.NO_CONTENT, removeARoleResponse.getHttpStatusCode());
        Either<FailureResponse, SuccessResponse> findUserAfterRemovingARole = findUser(username);

        //Then
        assertTrue(findUserAfterRemovingARole.isRight());
        assertEquals(userRoles.size() - 1, findUserAfterRemovingARole.get().getBody().get("roles").size());

        //When you remove all roles, add some roles and update status
        ApiResponse removeAllRolesResponse = removeAllRoles(username);
        assertEquals(HttpStatus.NO_CONTENT, removeAllRolesResponse.getHttpStatusCode());
        Either<FailureResponse, SuccessResponse> findUserResponseAfterRemovingAllRoles = findUser(username);
        assertEquals(0, findUserResponseAfterRemovingAllRoles.get().getBody().get("roles").size());

        //finally when you delete
        ApiResponse deleteUserResponse = deleteUser(username);
        assertEquals(HttpStatus.NO_CONTENT, deleteUserResponse.getHttpStatusCode());
    }

    @Test
    void createUserWhenCreateUserDTOIsNull() {
        //Given a null CreateUserRequest object
        CreateUserRequest createUserRequest = null;

        //When - a user is created
        Either<FailureResponse, SuccessResponse> createUserResponse = createUser(createUserRequest);

        //Then
        assertTrue(createUserResponse.isLeft());
        assertEquals(HttpStatus.BAD_REQUEST, createUserResponse.getLeft().getApiResponse().getHttpStatusCode());
        assertEquals(BAD_REQUEST_INVALID_PARAMS_CODE, createUserResponse.getLeft().getApiResponse().getResponseCode());
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
        return userService.setPassword(username, password);
    }

    private ApiResponse changePassword(String username, char[] currentPassword, char[] newPassword) {
        return userService.changePassword(username, currentPassword, newPassword);
    }

    private ApiResponse addRoles(String username, Set<UserRole> roles) {
        return userService.addRoles(username, roles);
    }

    private ApiResponse removeARole(String username, UserRole role) {
        return userService.removeRole(username, role);
    }

    private ApiResponse removeAllRoles(String username) {
        return userService.removeAllRoles(username);
    }

    private ApiResponse updateStatus(String username, UserStatus status) {
        return userService.updateStatus(username, status);
    }
}