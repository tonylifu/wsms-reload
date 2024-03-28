package com.lifu.wsms.reload.repository;

import com.lifu.wsms.reload.entity.student.StudentNumber;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentNumberRepository extends JpaRepository<StudentNumber, Long> {
    @Query("SELECT s FROM StudentNumber s WHERE s.currentYear = :currentYear ORDER BY s.createdAt DESC")
    List<StudentNumber> findLastCreatedStudentNumberByCurrentYear(@Param("currentYear") int currentYear);
}
