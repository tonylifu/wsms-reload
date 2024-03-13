package com.lifu.wsms.reload.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Address {
    private String houseNumber;
    private String streetName;
    private String area;
    private String localGovtArea;
    private String state;
    private String country;

}
