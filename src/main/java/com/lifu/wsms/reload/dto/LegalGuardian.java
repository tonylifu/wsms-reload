package com.lifu.wsms.reload.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegalGuardian {
    private boolean isBiologicalParentListed;
    private String father;
    private Contact fatherContactInformation;
    private String mother;
    private Contact motherContactInformation;
}
