package com.lifu.wsms.reload.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "role_permissions")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_permissions_sequence")
    @SequenceGenerator(name = "role_permissions_sequence", sequenceName = "role_permissions_id_sequence",
            allocationSize = 1)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_name", unique = true)
    private String name;
}
