package com.lifu.wsms.reload.controller.user;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.lifu.wsms.reload.dto.request.user.*;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static com.lifu.wsms.reload.controller.user.UserController.*;
import static com.lifu.wsms.reload.util.UserTestUtil.getCreateUserDTO;
import static com.lifu.wsms.reload.util.UserTestUtil.getUpdateUserDTO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateAndReadUser() {
        //Given - CREATE/READ
        var createUserRequest = getCreateUserDTO();

        //When
        ResponseEntity<String> createUserResponseEntity = restTemplate.postForEntity(USER_PATH, createUserRequest, String.class);

        //Then
        assertEquals(HttpStatus.CREATED, createUserResponseEntity.getStatusCode());

        //When
        String location = createUserResponseEntity.getHeaders().getFirst(LOCATION);
        ResponseEntity<String> getUserResponseEntity = restTemplate.getForEntity(location, String.class);

        //Then
        assertEquals(HttpStatus.OK, getUserResponseEntity.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(getUserResponseEntity.getBody());
        String username = documentContext.read("$.body.username");
        String email = documentContext.read("$.body.email");
        JSONArray roles = documentContext.read("$.body.roles");

        assertEquals(createUserRequest.getUsername().toUpperCase(), username);
        assertEquals(createUserRequest.getEmail(), email);
        assertEquals(createUserRequest.getRoles().size(), roles.size());

        //Given - UPDATE
        var updateUserRequest = getUpdateUserDTO();
        updateUserRequest.setUsername(username);
        assertEquals(createUserRequest.getUsername().toUpperCase(), updateUserRequest.getUsername());
        assertNotEquals(createUserRequest.getMobile(), updateUserRequest.getMobile());
        assertNotEquals(createUserRequest.getEmail(), updateUserRequest.getEmail());

        //When
        restTemplate.put(USER_PATH, updateUserRequest);
        ResponseEntity<String> getUserResponseEntityAfterUpdate = restTemplate.getForEntity(location, String.class);

        //Then
        DocumentContext documentContextAfterUpdate = JsonPath.parse(getUserResponseEntityAfterUpdate.getBody());
        String usernameAterUpdate = documentContextAfterUpdate.read("$.body.username");
        String emailAfterUpdate = documentContextAfterUpdate.read("$.body.email");
        JSONArray rolesAfterUpdate = documentContextAfterUpdate.read("$.body.roles");

        Set<UserRole> updatedRoles = new HashSet<>();
        updatedRoles.addAll(createUserRequest.getRoles());
        updatedRoles.addAll(updateUserRequest.getRoles() != null ? updateUserRequest.getRoles() : Set.of());

        assertEquals(updateUserRequest.getUsername().toUpperCase(), usernameAterUpdate);
        assertEquals(updateUserRequest.getEmail(), emailAfterUpdate);
        assertEquals(updatedRoles.size(), rolesAfterUpdate.size());

        //Given - Set Password
        char[] password = "Password@1234".toCharArray();
        PasswordRequest passwordSetRequest = new PasswordRequest();
        passwordSetRequest.setPassword(password);

        //And
        boolean isPasswordSet = documentContextAfterUpdate.read("$.body.passwordSet");
        assertFalse(isPasswordSet);

        //When
        restTemplate.put(location + SET_PASSWORD_PATH, passwordSetRequest);
        ResponseEntity<String> getUserResponseEntityAfterPasswordSet = restTemplate.getForEntity(location, String.class);

        //Then
        DocumentContext documentContextAfterPasswordSet = JsonPath.parse(getUserResponseEntityAfterPasswordSet.getBody());
        boolean passwordSet = documentContextAfterPasswordSet.read("$.body.passwordSet");
        String setEncodedPassword = documentContextAfterPasswordSet.read("$.body.password");
        assertTrue(passwordSet);
        assertTrue(passwordEncoder.matches(new String(password), setEncodedPassword));

        //When - Change Password
        // Given - char[] currentPassword = password;
        char[] newPassword = "pAssWORD@321".toCharArray();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword(password);
        changePasswordRequest.setNewPassword(newPassword);

        //Then
        restTemplate.put(location + CHANGE_PASSWORD_PATH, changePasswordRequest);
        ResponseEntity<String> getUserResponseEntityAfterPasswordChange = restTemplate.getForEntity(location, String.class);

        DocumentContext documentContextAfterPasswordChange = JsonPath.parse(getUserResponseEntityAfterPasswordChange.getBody());
        boolean passwordChange = documentContextAfterPasswordChange.read("$.body.passwordSet");
        String newEncodedPassword = documentContextAfterPasswordChange.read("$.body.password");
        assertTrue(passwordChange);
        assertTrue(passwordEncoder.matches(new String(newPassword), newEncodedPassword));
        assertFalse(passwordEncoder.matches(new String(password), newEncodedPassword));

        //Given
        assertEquals(UserStatus.CREATED.name(),  documentContextAfterPasswordChange.read("$.body.status"));

        //When
        UserStatusUpdateRequest statusUpdateRequest = new UserStatusUpdateRequest();
        statusUpdateRequest.setStatus(UserStatus.ACTIVE);
        restTemplate.put(location + CHANGE_STATUS_PATH, statusUpdateRequest);
        ResponseEntity<String> getUserResponseEntityAfterStatusUpdate = restTemplate.getForEntity(location, String.class);

        //Then
        DocumentContext documentContextAfterStatusUpdate = JsonPath.parse(getUserResponseEntityAfterStatusUpdate.getBody());
        assertEquals(UserStatus.ACTIVE.name(),  documentContextAfterStatusUpdate.read("$.body.status"));

        //Given - a set of Roles => Add Roles
        Set<UserRole> someRoles = Set.of(UserRole.ADMIN, UserRole.BURSAR, UserRole.CASHIER);
        UpdateUserRoles updateUserRoles = new UpdateUserRoles();
        updateUserRoles.setRoles(someRoles);

        //When you add roles
        restTemplate.put(location + ADD_ROLES_PATH, updateUserRoles);
        ResponseEntity<String> userAfterAddRoles = restTemplate.getForEntity(location, String.class);

        //Then
        DocumentContext documentContextAfterAddRoles = JsonPath.parse(userAfterAddRoles.getBody());
        JSONArray rolesAfterAddRoles = documentContextAfterAddRoles.read("$.body.roles");
        updatedRoles.addAll(someRoles);
        assertEquals(updatedRoles.size(), rolesAfterAddRoles.size());
    }
}
