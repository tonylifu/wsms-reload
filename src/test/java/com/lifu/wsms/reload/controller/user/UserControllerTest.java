package com.lifu.wsms.reload.controller.user;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static com.lifu.wsms.reload.controller.user.UserController.USER_PATH;
import static com.lifu.wsms.reload.util.UserTestUtil.getCreateUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndReadUser() {
        //Given
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
    }
}
