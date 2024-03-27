package com.lifu.wsms.reload.config.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lifu.wsms.reload.api.StudentService;
import com.lifu.wsms.reload.repository.AccountRepository;
import com.lifu.wsms.reload.repository.StudentNumberRepository;
import com.lifu.wsms.reload.repository.StudentRepository;
import com.lifu.wsms.reload.service.student.StudentNumberGenerator;
import com.lifu.wsms.reload.service.student.StudentNumberService;
import com.lifu.wsms.reload.service.student.StudentRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class ServiceConfig {

    @Bean
    public StudentNumberService studentNumberService(final StudentNumberRepository studentNumberRepository,
                                                     final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new StudentNumberGenerator(studentNumberRepository, namedParameterJdbcTemplate);
    }
    @Bean
    public StudentService studentService(final StudentRepository studentRepository,
                                         final AccountRepository accountRepository,
                                         final ObjectMapper objectMapper,
                                         final StudentNumberService studentNumberService) {
        return new StudentRecord(studentRepository,
                accountRepository,
                objectMapper,
                studentNumberService);
    }
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
