package com.lifu.wsms.reload.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String houseNumber;
    private String streetName;
    private String area;
    private String localGovtArea;
    private String state;
    private String country;

}
