package com.lifu.wsms.reload.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SequenceResponse {
    private Long nextVal;
    private Long currentVal;
}
