package com.lifu.wsms.reload.repository;

import com.lifu.wsms.reload.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT e FROM User e WHERE e.username = :username")
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    @Modifying
    @Query("DELETE FROM User e WHERE e.username = :username")
    void deleteByUsername(@Param("username") String username);
}
