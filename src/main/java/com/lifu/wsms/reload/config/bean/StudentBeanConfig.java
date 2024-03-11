package com.lifu.wsms.reload.config.bean;

import com.lifu.wsms.reload.api.StudentService;
import com.lifu.wsms.reload.service.StudentRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentBeanConfig {
    @Bean
    public StudentService studentService() {
        return new StudentRecord();
    }
}
