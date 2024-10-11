package com.example.tournamentservice.config;

import java.beans.Customizer;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/error").permitAll() // default error page
                // Open access to GET requests for retrieving tournaments
                .requestMatchers(HttpMethod.GET, "/tournaments", "/tournaments/**").permitAll()
                // Admin-only access for creating or modifying tournaments
                .requestMatchers(HttpMethod.POST, "/tournaments").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tournaments/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasRole("ADMIN")
                .anyRequest().permitAll() // Allow all other requests
            )
            // Stateless REST API: no session creation
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API
            .formLogin(form -> form.disable()) // Disable form login
            .headers(header -> header.disable()); // Disable security headers for API responses
        return http.build();
    }
}
