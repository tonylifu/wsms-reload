package com.lifu.wsms.reload.entity.student;

import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.LegalGuardian;
import com.lifu.wsms.reload.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;

@Entity
@Table(name = "student")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String studentId;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private long dob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    //@Embedded
    @JdbcTypeCode(SqlTypes.JSON)
    private Address address;

    //@Embedded
    @JdbcTypeCode(SqlTypes.JSON)
    private Contact contact;

    //@Embedded
    @JdbcTypeCode(SqlTypes.JSON)
    private LegalGuardian legalGuardian;

    @Column(nullable = false)
    private String currentGrade;

    @Column(nullable = false)
    private boolean isDisabled;

    private String disabilityDetail;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private long lastUpdateAt;
}