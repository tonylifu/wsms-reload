package com.lifu.wsms.reload.dto.response.finance;

import com.lifu.wsms.reload.dto.response.student.StudentResponse;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAccountBalanceResponse {
    private BigDecimal accountBalance;
    private StudentResponse studentResponse;
}
