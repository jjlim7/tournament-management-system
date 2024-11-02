package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for the gateway
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/elo-ranking/api/**",
                                "/matchmaking/api/**",
                                "/tournament/api/**",
                                "/userclan/api/**"  // Allow access without authentication
                        ).permitAll()
                        .anyExchange().authenticated()  // Authenticate all other paths
                )
                .build();
    }
}