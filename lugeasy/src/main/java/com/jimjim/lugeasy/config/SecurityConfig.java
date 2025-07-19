package com.jimjim.lugeasy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // From 로그인 방식 disable (UsernamePasswordAuthenticationFilter 비활성화)
        http
                .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable (BasicAuthenticationFilter 비활성화)
        http
                .httpBasic((auth) -> auth.disable());

        return http.build();
    }
}