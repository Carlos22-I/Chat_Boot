package com.tutorvirtual.tutorvirtual_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/usuarios/login",
                "/api/usuarios/register",
                "/api/chat/**")
                .permitAll()
                .anyRequest().permitAll() // 👈 IMPORTANTE POR AHORA
            )
            .formLogin(form -> form.disable()) // 👈 MUY IMPORTANTE
            .httpBasic(basic -> basic.disable()); // 👈 MUY IMPORTANTE

        return http.build();
    }
}
