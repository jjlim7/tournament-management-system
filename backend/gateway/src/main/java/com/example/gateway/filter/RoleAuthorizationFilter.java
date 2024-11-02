package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public class RoleAuthorizationFilter implements GatewayFilter {

    private static final Logger logger = LoggerFactory.getLogger(RoleAuthorizationFilter.class);
    private final List<String> allowedRoles;

    public RoleAuthorizationFilter(List<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("RoleAuthorizationFilter: Checking roles for request {}", exchange.getRequest().getURI());

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> {
                    var authentication = context.getAuthentication();
                    if (authentication != null) {
                        boolean authorized = authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .anyMatch(allowedRoles::contains);

                        if (authorized) {
                            logger.info("Authorization successful for roles: {}", allowedRoles);
                            return chain.filter(exchange);
                        }
                    }
                    logger.warn("Authorization failed. Required roles: {}", allowedRoles);
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                });
    }
}
