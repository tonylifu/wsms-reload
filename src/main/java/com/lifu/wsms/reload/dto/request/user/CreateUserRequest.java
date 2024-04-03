package com.lifu.wsms.reload.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String designation;
    private String email;
    private String mobile;
    private UserStatus status;
    private String dob;
    private Gender gender;
    private Address address;
    private Contact contact;
}
