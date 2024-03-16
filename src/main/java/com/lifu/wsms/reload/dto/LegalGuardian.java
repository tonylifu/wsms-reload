package com.lifu.wsms.reload.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalGuardian {
    private boolean isBiologicalParentListed;
    private String father;
    private Contact fatherContactInformation;
    private String mother;
    private Contact motherContactInformation;
}
