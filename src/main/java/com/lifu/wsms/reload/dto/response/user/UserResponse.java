package com.lifu.wsms.reload.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.entity.user.UserGroup;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserRolesGroup;
import com.lifu.wsms.reload.enums.UserStatus;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private String username;
    private String password;
    private UserStatus status;
    private String designation;
    private String email;
    private String mobile;
    private String fullName;
    private String dob;
    private Gender gender;
    private Address address;
    private Contact contact;
    private UserRolesGroup userGroup;
    private long createdAt;
    private long lastModifiedAt;
    private String actionBy;
    private String lastActionBy;
    private boolean passwordSet;
    private long lastPasswordChangeAt;
}
