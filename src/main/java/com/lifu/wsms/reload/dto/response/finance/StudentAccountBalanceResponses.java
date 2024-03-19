package com.lifu.wsms.reload.dto.response.finance;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAccountBalanceResponses {
    List<StudentAccountBalanceResponse> studentAccounts;
    Long totalCount;
}
