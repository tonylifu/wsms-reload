package com.lifu.wsms.reload.controller.user;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.lifu.wsms.reload.dto.request.user.PasswordSetRequest;
import com.lifu.wsms.reload.enums.UserRole;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static com.lifu.wsms.reload.controller.user.UserController.USER_PATH;
import static com.lifu.wsms.reload.util.UserTestUtil.getCreateUserDTO;
import static com.lifu.wsms.reload.util.UserTestUtil.getUpdateUserDTO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndReadUser() {
        //Given - CREATE/READ
        var createUserRequest = getCreateUserDTO();

        //When
        ResponseEntity<String> createUserResponseEntity = restTemplate.postForEntity(USER_PATH, createUserRequest, String.class);

        //Then
        assertEquals(HttpStatus.CREATED, createUserResponseEntity.getStatusCode());

        //When
        String location = createUserResponseEntity.getHeaders().getFirst("location");
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
        String password = "Password@1234";
        PasswordSetRequest passwordSetRequest = new PasswordSetRequest();
        passwordSetRequest.setPassword(password);

        //And
        boolean isPasswordSet = documentContextAfterUpdate.read("$.body.passwordSet");
        assertFalse(isPasswordSet);

        //When
        restTemplate.put(location + "/password", passwordSetRequest);
        ResponseEntity<String> getUserResponseEntityAfterPasswordSet = restTemplate.getForEntity(location, String.class);
        DocumentContext documentContextAfterPasswordSet = JsonPath.parse(getUserResponseEntityAfterPasswordSet.getBody());
        boolean passwordSet = documentContextAfterPasswordSet.read("$.body.passwordSet");
        assertTrue(passwordSet);
    }
}
