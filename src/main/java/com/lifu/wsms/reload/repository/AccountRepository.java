package com.lifu.wsms.reload.repository;

import com.lifu.wsms.reload.entity.finance.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountBalance, Long> {
}
