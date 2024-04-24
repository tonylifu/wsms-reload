package com.lifu.wsms.reload.controller.user;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import static com.lifu.wsms.reload.controller.user.UserController.USER_PATH;
import static com.lifu.wsms.reload.util.UserTestUtil.getCreateUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndReadUser() {
        //Given
        var createUserRequest = getCreateUserDTO();

        //When
        ResponseEntity<Either> createUserResponseEntity = restTemplate.postForEntity(USER_PATH, createUserRequest, Either.class);

        //Then
        assertTrue(createUserResponseEntity.getBody().isRight());
        assertEquals(HttpStatus.CREATED, createUserResponseEntity.getStatusCode());

        //When
        String location = createUserResponseEntity.getHeaders().getFirst("location");
        System.out.println(location);
        ResponseEntity<Either> getUserResponseEntity = restTemplate.getForEntity(location, Either.class);

        //Then
        assertEquals(HttpStatus.OK, getUserResponseEntity.getStatusCode());
        assertTrue(getUserResponseEntity.getBody().isRight());

        DocumentContext documentContext = JsonPath.parse(getUserResponseEntity.getBody());
        String username = documentContext.read("$.username");
        String email = documentContext.read("$.email");

        assertEquals(createUserRequest.getUsername(), username);
        assertEquals(createUserRequest.getEmail(), email);
    }
}
