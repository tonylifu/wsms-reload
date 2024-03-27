package com.lifu.wsms.reload.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthConverter jwtAuthConverter;

//    @Order(1)
//    @Bean
//    public SecurityFilterChain jwtSecurityFilterChain2(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize ->
//                        authorize.requestMatchers("/oauth/token")
//                                .permitAll()
//                                .requestMatchers("/api/v1/students", "/api/v1/students/**")
//                                .permitAll()
//                                .requestMatchers("/api/v1/accounts", "/api/v1/accounts/**")
//                                .permitAll()
//                                .requestMatchers("/api/v1/sequence")
//                                .permitAll()
//                                .anyRequest()
//                                .permitAll());
//        return http.build();
//    }

//    @Order(2)
//    @Bean
//    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http,
//                                                      @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(anyRequest ->
//                        anyRequest.requestMatchers("*")
//                                .hasRole("client_admin")
//                                .anyRequest()
//                                .authenticated())
//                .oauth2ResourceServer(jwt -> jwt
//                        .jwt(cfg -> cfg.jwkSetUri(jwkSetUri))
//                        .jwt(jwtConfigurer -> jwtConfigurer
//                                .jwtAuthenticationConverter(jwtAuthConverter))
//                )
//                .sessionManagement(sessionManager -> sessionManager
//                        .sessionCreationPolicy(STATELESS));
//        return http.build();
//    }

    @Order(3)
    @Bean
    public SecurityFilterChain jwtSecurityFilterChain3(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().permitAll());
        return http.build();
    }
}
