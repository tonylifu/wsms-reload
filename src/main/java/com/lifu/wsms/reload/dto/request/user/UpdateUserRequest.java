package com.lifu.wsms.reload.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String designation;
    private String email;
    private String mobile;
    private Set<UserRole> roles;
    UserStatus status;
}
