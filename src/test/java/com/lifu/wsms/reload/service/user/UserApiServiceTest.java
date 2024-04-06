package com.lifu.wsms.reload.service.user;

import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.entity.user.User;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.util.UserTestUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserApiServiceTest {

    @Test
    void validateCreateUser() {
        //Given
        var validCreateUserRequest = UserTestUtil.getCreateUserDTO();
        CreateUserRequest nullCreateUserRequest = null;
        var noUsernameCreateUserRequest = UserTestUtil.getCreateUserDTO();
        noUsernameCreateUserRequest.setUsername(null);

        //Wehen
        Either<FailureResponse, Boolean> validUserValidateResponse = UserApiService.validateCreateUser(validCreateUserRequest);
        Either<FailureResponse, Boolean> nullUserValidateResponse = UserApiService.validateCreateUser(nullCreateUserRequest);
        Either<FailureResponse, Boolean> noUsernameValidateResponse = UserApiService.validateCreateUser(noUsernameCreateUserRequest);

        //Then
        assertTrue(validUserValidateResponse.isRight());
        assertTrue(nullUserValidateResponse.isLeft());
        assertTrue(noUsernameValidateResponse.isLeft());
    }

    @Test
    void validateUpdateUser() {
        //Given
        UpdateUserRequest nullUpdateUserRequest = null;

        //When
        Either<FailureResponse, Boolean> nullValidateUpdateResponse = UserApiService.validateUpdateUser(nullUpdateUserRequest);


        //Then
        assertTrue(nullValidateUpdateResponse.isLeft());
    }

    @Test
    void populateUserForUpdate() {
        //Given
        var userEntity = UserTestUtil.getUserEntity();
        var updateRequest = UserTestUtil.getUpdateUserDTO();
        assertEquals("test1", userEntity.getUsername());
        assertEquals("user2", updateRequest.getUsername());
        assertNull(userEntity.getGender());
        assertNotEquals(updateRequest.getUsername(), userEntity.getUsername());

        //When
        User user = UserApiService.populateUserForUpdate(userEntity, updateRequest);

        //Then
        assertEquals(updateRequest.getUsername(), user.getUsername());
        assertEquals("user2", user.getUsername());
        assertEquals(Gender.MALE.name(), user.getGender().name());
    }
}