package com.lifu.wsms.reload.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifu.wsms.reload.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStatusUpdateRequest {
    private UserStatus status;
}
