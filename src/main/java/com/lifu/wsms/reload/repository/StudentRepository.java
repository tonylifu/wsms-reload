package com.lifu.wsms.reload.repository;

import com.lifu.wsms.reload.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);

    @Modifying
    @Query("DELETE FROM Student e WHERE e.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") String studentId);
}
