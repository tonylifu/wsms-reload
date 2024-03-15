package com.lifu.wsms.reload.repository;

import com.lifu.wsms.reload.entity.finance.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountBalance, Long> {
    @Modifying
    @Query("DELETE FROM AccountBalance e WHERE e.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") String studentId);
}
