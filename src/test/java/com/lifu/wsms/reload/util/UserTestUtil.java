package com.lifu.wsms.reload.util;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.Item;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.entity.user.Role;
import com.lifu.wsms.reload.entity.user.User;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.enums.UserPermission;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import java.time.LocalDate;
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
                .roles(Set.of(UserRole.ADMIN, UserRole.BURSAR))
                .build();
    }

    public static UpdateUserRequest getUpdateUserDTO() {
        return UpdateUserRequest.builder()
                .username("user2")
                .mobile("01234456677")
                .email("test2@test.com")
                .designation("Bursar")
                .gender(Gender.MALE)
                .build();
    }

    public static User getUserEntity() {
        User user = new User();
        user.setUsername("test1");
        user.setFirstName("David");
        user.setMobile("Owogoga");
        user.setLastName("Lifu");
        user.setDob(AppUtil.convertLocalDateToLong(LocalDate.of(2010, 1, 1)));
        user.setEmail("test@test.com");
        user.setMobile("07766433489");
        user.setRoles(Set.of(new Item()));
        return user;
    }

    public static Role getRoleEntity() {
        Role role = new Role();
        role.setName(UserRole.ADMIN.name());
        role.setPermissions(Set.of(
                Item.builder().name(UserPermission.STUDENT_CREATE.name()).build(),
                Item.builder().name(UserPermission.STUDENT_READ.name()).build(),
                Item.builder().name(UserPermission.STUDENT_UPDATE.name()).build(),
                Item.builder().name(UserPermission.STUDENT_DELETE.name()).build(),
                Item.builder().name(UserPermission.USER_CREATE.name()).build(),
                Item.builder().name(UserPermission.USER_READ.name()).build()
        ));
        return role;
    }
}
