package com.lifu.wsms.reload.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LegalGuardian {
    private boolean isBiologicalParentListed;
    private String father;
    private Contact fatherContactInformation;
    private String mother;
    private Contact motherContactInformation;
}
