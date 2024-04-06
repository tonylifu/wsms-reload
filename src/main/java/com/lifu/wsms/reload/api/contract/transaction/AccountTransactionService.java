package com.lifu.wsms.reload.api.contract.transaction;

import com.lifu.wsms.reload.dto.response.ApiResponse;

import java.math.BigDecimal;

public interface AccountTransactionService {
    ApiResponse postTransaction(String destinationAccount, BigDecimal amount);
    ApiResponse fundTransfer(String sourceAccount, String destinationAccount, BigDecimal amount);
}
