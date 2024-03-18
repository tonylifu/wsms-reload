package com.lifu.wsms.reload.entity.finance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "account_balance")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_balance_sequence")
    @SequenceGenerator(name = "account_balance_sequence", sequenceName = "account_balancet_sequence",
            allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String studentId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private long lastUpdateAt;
}