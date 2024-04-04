package com.lifu.wsms.reload.entity.user;

import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_id_sequence",
            allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String mobile;

    private char[] password;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OAuthToken> refreshTokens;

    private String designation;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private long lastUpdatedAt;

    @Column(nullable = false)
    private String actionBy;

    @Column(nullable = false)
    private String lastActionBy;

    private boolean isPasswordSet;

    @Column(nullable = false)
    private long lastPasswordChangedAt;
}