package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.util.UserTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateUserRequestToUserMapperTest {

    @Test
    void toUser() {
        //Given a userRequest DTO
        var userRequest = UserTestUtil.getCreateUserDTO();

        //When mapped to a user object
        var user = CreateUserRequestToUserMapper.INSTANCE.toUser(userRequest);

        //Then every field should be transferred accurately
        assertEquals(userRequest.getFirstName(), user.getFirstName());
        assertEquals(userRequest.getMiddleName(), user.getMiddleName());
        assertEquals(userRequest.getLastName(), user.getLastName());
        assertEquals(userRequest.getEmail(), user.getEmail());
        assertEquals(userRequest.getStatus(), user.getStatus());
        assertEquals(userRequest.getDesignation(), user.getDesignation());
        assertEquals(AppUtil.convertLocalDateToLong(LocalDate.parse(userRequest.getDob())), user.getDob());
        assertEquals(userRequest.getAddress(), user.getAddress());
        assertEquals(userRequest.getContact(), user.getContact());
        assertEquals(userRequest.getMobile(), user.getMobile());
        assertEquals(userRequest.getUsername(), user.getUsername());
        assertEquals(userRequest.getGender(), user.getGender());
    }
}