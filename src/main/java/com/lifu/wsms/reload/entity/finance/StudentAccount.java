package com.lifu.wsms.reload.entity.finance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "student_accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_accounts_sequence")
    @SequenceGenerator(name = "student_accounts_sequence", sequenceName = "student_accounts_id_sequence",
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

    @Version
    private int version;

    @Column(nullable = false)
    private String lastActionBy;
}