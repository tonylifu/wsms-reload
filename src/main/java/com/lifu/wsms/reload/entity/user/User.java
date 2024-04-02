package com.lifu.wsms.reload.entity.user;

import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_id_sequence",
            allocationSize = 1)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String mobile;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    private String designation;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private long lastUpdateAt;

    @Column(nullable = false)
    private String actionBy;

    @Column(nullable = false)
    private String lastActionBy;

    private boolean isPasswordSet;

    @Column(nullable = false)
    private long lastPasswordChangeAt;
}