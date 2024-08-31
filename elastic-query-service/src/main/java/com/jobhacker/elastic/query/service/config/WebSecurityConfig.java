//package com.jobhacker.elastic.query.service.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/**").permitAll()  // Disable security for all endpoints
//                )
//                .csrf(csrf -> csrf.disable()); // Disable CSRF protection using lambda expression
//
//        return http.build();
//    }
//}