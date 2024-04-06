package com.lifu.wsms.reload.service.student;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.contract.student.StudentNumberService;
import com.lifu.wsms.reload.entity.student.StudentNumber;
import com.lifu.wsms.reload.repository.StudentNumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class StudentNumberGenerator implements StudentNumberService {
    private final StudentNumberRepository studentNumberRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String generateNextStudentId(int currentYear) {
        Optional<StudentNumber> studentNumberResult = findLastCreatedStudentByCurrentYear(currentYear);
        log.info("::::StudentNumber Response::::\n{}", studentNumberResult);
        int nextStudentNumber = studentNumberResult.map(studentNumber -> studentNumber.getStudentNumber() + 1).orElse(1);
        String formattedStudentNumber = String.format("%04d", nextStudentNumber);
        persistStudentNumber(currentYear, formattedStudentNumber);
        return formattedStudentNumber.toUpperCase();
    }

    @Override
    public void deleteStudentNumberById(Long id) {
        studentNumberRepository.deleteById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Optional<StudentNumber> findLastCreatedStudentByCurrentYear(int currentYear) {
        String sql = "SELECT * FROM student_numbers WHERE current_year = :currentYear ORDER BY id DESC LIMIT 1";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("currentYear", currentYear);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, params, BeanPropertyRowMapper.newInstance(StudentNumber.class)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected void persistStudentNumber(int currentYear, String studentNumber) {
        // Validate parameters
        validateParameters(currentYear, studentNumber);

        String sql = "INSERT INTO student_numbers (current_year, student_number, created_at, action_by, id) " +
                "VALUES (:currentYear, :studentNumber, :createdAt, :actionBy, :id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("currentYear", currentYear)
                .addValue("studentNumber", Integer.parseInt(studentNumber))
                .addValue("createdAt", AppUtil.convertLocalDateToLong(LocalDate.now()))
                .addValue("actionBy", AppUtil.getUserFromSecurityContext())
                .addValue("id", Long.parseLong(String.format("%s%s", currentYear, studentNumber)));

        try {
            // Execute the SQL insert operation
            int rowsAffected = namedParameterJdbcTemplate.update(sql, params);

            if (rowsAffected != 1) {
                // Handle unexpected situation when rows affected is not 1
                throw new RuntimeException("Failed to persist StudentNumber");
            }
        } catch (DataAccessException e) {
            // Handle data access exception (e.g., database connection issue, SQL syntax error)
            throw new RuntimeException("Failed to persist StudentNumber", e);
        }
    }

    private void validateParameters(int currentYear, String studentNumber) {
        if (currentYear <= 0) {
            throw new IllegalArgumentException("currentYear must be greater than 0");
        }
        try {
            int number = Integer.parseInt(studentNumber);
            if (number <= 0) {
                throw new IllegalArgumentException("studentNumber must be a positive integer");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("studentNumber must be a valid integer");
        }
    }

}
