package com.lifu.wsms.reload.entity.student;

import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.LegalGuardian;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students_sequence")
    @SequenceGenerator(name = "students_sequence", sequenceName = "students_id_sequence",
            allocationSize = 1)
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

    @JdbcTypeCode(SqlTypes.JSON)
    private Address address;

    @JdbcTypeCode(SqlTypes.JSON)
    private Contact contact;

    @JdbcTypeCode(SqlTypes.JSON)
    private LegalGuardian legalGuardian;

    @Column(nullable = false)
    private String currentGrade;

    @Column(nullable = false)
    private boolean isDisabled;

    private String disabilityDetails;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private long lastUpdateAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus studentStatus;

    @Column(nullable = false)
    private String actionBy;

    @Column(nullable = false)
    private String lastActionBy;
}