package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.util.UserTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateUserRequestToUserMapperTest {

    @Test
    void toUser() {
        //Given
        var userRequest = UserTestUtil.getCreateUserDTO();

        //When
        var user = CreateUserRequestToUserMapper.INSTANCE.toUser(userRequest);
        System.out.println(user);

        //Then
        assertEquals(userRequest.getFirstName(), user.getFirstName());
        assertEquals(userRequest.getStatus(), user.getStatus());
    }
}