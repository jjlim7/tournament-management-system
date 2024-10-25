package com.example.gateway.filter;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class RoleAuthorizationFilter implements GatewayFilter {

    private final List<String> allowedRoles;

    // Constructor to initialize the allowed roles for the filter
    public RoleAuthorizationFilter(List<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(this::authorize)
                .flatMap(isAuthorized -> {
                    if (isAuthorized) {
                        return chain.filter(exchange);  // User authorized, proceed with the request
                    } else {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);  // 403 Forbidden
                        return exchange.getResponse().setComplete();
                    }
                });
    }

    // Authorization logic: Check if the authenticated user has one of the required roles
    private Mono<Boolean> authorize(SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null) {
            return Mono.just(
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .map(role -> role.replace("ROLE_", ""))  // Remove the "ROLE_" prefix
                            .anyMatch(allowedRoles::contains)  // Check if user has one of the allowed roles
            );
        }
        return Mono.just(false);  // No authentication found, deny access
    }
}
