package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.response.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.lifu.wsms.reload.api.AppUtil.*;
import static com.lifu.wsms.reload.util.UserTestUtil.getUserEntity;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserToUserResponseMapperTest {

    @Test
    void toUserResponse() {
        //Given a user entity
        var user = getUserEntity();

        //When
        UserResponse userResponse = UserToUserResponseMapper.INSTANCE.toUserResponse(user);

        //Then
        assertEquals(user.getUsername(), userResponse.getUsername());
        assertEquals(user.getEmail(), userResponse.getEmail());
        assertEquals(getFullName(user.getFirstName(), user.getMiddleName(), user.getLastName()), userResponse.getFullName());
        assertEquals(String.valueOf(convertLongToLocalDate(user.getDob())), userResponse.getDob());
        assertEquals(String.valueOf(convertLongToLocalDateTime(user.getLastUpdatedAt())), userResponse.getLastUpdatedAt());
        assertEquals(user.getMobile(), userResponse.getMobile());
        assertEquals(user.getRoles().size(), userResponse.getRoles().size());
    }

}