package com.lifu.wsms.reload.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.Item;
import com.lifu.wsms.reload.enums.Gender;
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
    private char[] password;
    private UserStatus status;
    private String designation;
    private String email;
    private String mobile;
    private String fullName;
    private String dob;
    private Gender gender;
    private Address address;
    private Contact contact;
    private String createdAt;
    private String lastUpdatedAt;
    private String actionBy;
    private String lastActionBy;
    private boolean passwordSet;
    private String lastPasswordChangedAt;
    private Set<Item> roles;
//    private List<OAuthToken> refreshTokens;
}
