package com.lifu.wsms.reload.config.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lifu.wsms.reload.api.SequenceService;
import com.lifu.wsms.reload.api.StudentService;
import com.lifu.wsms.reload.repository.AccountRepository;
import com.lifu.wsms.reload.repository.StudentRepository;
import com.lifu.wsms.reload.service.SequenceServiceGenerator;
import com.lifu.wsms.reload.service.student.StudentRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ServiceConfig {
    @Bean
    public StudentService studentService(final StudentRepository studentRepository,
                                         final AccountRepository accountRepository,
                                         final ObjectMapper objectMapper) {
        return new StudentRecord(studentRepository, accountRepository, objectMapper);
    }
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public SequenceService sequenceService(final JdbcTemplate jdbcTemplate) {
        return new SequenceServiceGenerator(jdbcTemplate);
    }
}
