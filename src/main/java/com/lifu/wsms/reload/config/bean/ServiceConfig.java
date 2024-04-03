package com.lifu.wsms.reload.config.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lifu.wsms.reload.api.contract.StudentService;
import com.lifu.wsms.reload.api.contract.user.UserService;
import com.lifu.wsms.reload.repository.AccountRepository;
import com.lifu.wsms.reload.repository.StudentNumberRepository;
import com.lifu.wsms.reload.repository.StudentRepository;
import com.lifu.wsms.reload.service.student.StudentNumberGenerator;
import com.lifu.wsms.reload.api.contract.StudentNumberService;
import com.lifu.wsms.reload.service.student.StudentRecord;
import com.lifu.wsms.reload.service.user.UserRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    public UserService userService(final ObjectMapper objectMapper,
                                   final PasswordEncoder passwordEncoder) {
        return new UserRecord(objectMapper, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
