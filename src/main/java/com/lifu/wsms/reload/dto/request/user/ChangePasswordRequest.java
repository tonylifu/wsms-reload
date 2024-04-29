package com.lifu.wsms.reload.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePasswordRequest {
    private char[] currentPassword;
    private char[] newPassword;
}
