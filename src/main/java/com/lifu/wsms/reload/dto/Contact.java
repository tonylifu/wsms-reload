package com.lifu.wsms.reload.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Contact {
    private String email;
    private String mobilePhone;
    private String telephone;

}
