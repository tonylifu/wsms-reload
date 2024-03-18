package com.lifu.wsms.reload.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    private String houseNumber;
    private String streetName;
    private String area;
    private String localGovtArea;
    private String state;
    private String country;

}
