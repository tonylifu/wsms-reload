package com.lifu.wsms.reload.util;

import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;

import java.util.Set;

public class UserTestUtil {
    private UserTestUtil(){}

    public static CreateUserRequest getCreateUserDTO() {
        return CreateUserRequest.builder()
                .username("user1")
                .mobile("077664333489")
                .email("test1@test.com")
                .designation("Administrator")
                .firstName("David")
                .middleName("Owogoga")
                .lastName("Lifu")
                .dob("2010-01-01")
                .gender(Gender.MALE)
                .status(UserStatus.CREATED)
                .build();
    }

    public static UpdateUserRequest getUpdateUserDTO() {
        return UpdateUserRequest.builder()
                .username("user2")
                .mobile("01234456677")
                .email("test2@test.com")
                .designation("Bursar")
                .build();
    }
}
