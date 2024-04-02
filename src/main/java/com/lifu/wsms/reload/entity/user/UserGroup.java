package com.lifu.wsms.reload.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_groups")
@Data
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_groups_sequence")
    @SequenceGenerator(name = "user_groups_sequence", sequenceName = "user_groups_id_sequence",
            allocationSize = 1)
    @Column(name = "user_group_id")
    private Long id;

    @Column(name = "user_group_name", unique = true)
    private String name;

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group_roles",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
