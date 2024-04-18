package com.lifu.wsms.reload.entity.user;

import com.lifu.wsms.reload.dto.Item;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_sequence")
    @SequenceGenerator(name = "roles_sequence", sequenceName = "roles_id_sequence",
            allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    private Set<Item> permissions = new HashSet<>();

    private long createdAt;

    private long lastUpdatedAt;

    @Column(nullable = false)
    private String actionBy;

    @Column(nullable = false)
    private String lastActionBy;
}
