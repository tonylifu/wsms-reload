package com.lifu.wsms.reload.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/oauth/token")
                                .permitAll()
                                .requestMatchers("/api/v1/students", "/api/v1/students/*")
                                .permitAll()
                                .requestMatchers("/api/v1/accounts", "/api/v1/accounts/*")
                                .permitAll()
                                .requestMatchers("/api/v1/sequence")
                                .permitAll()
                                .anyRequest()
                                .permitAll());
        return http.build();
    }
}
