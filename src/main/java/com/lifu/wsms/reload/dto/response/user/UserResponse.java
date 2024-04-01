package com.lifu.wsms.reload.dto.response.user;

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
public class UserResponse {
    private String username;
    private String password;
    UserStatus status;
    private String designation;
    private String email;
    private String mobile;
    private Set<UserRole> roles;
    private long createdAt;
    private long lastModifiedAt;
    private String actionBy;
    private String lastActionBy;
    private boolean passwordSet;
    private long lastPasswordChangeAt;
}
