package com.lifu.wsms.reload.repository;

import com.lifu.wsms.reload.entity.user.Role;
import com.lifu.wsms.reload.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Modifying
    @Query(value = "DELETE FROM user_roles WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(Long userId);

    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
