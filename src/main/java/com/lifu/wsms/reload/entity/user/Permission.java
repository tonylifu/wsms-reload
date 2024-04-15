package com.lifu.wsms.reload.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Data
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_permissions_sequence")
    @SequenceGenerator(name = "role_permissions_sequence", sequenceName = "role_permissions_id_sequence",
            allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}
