package com.example.gateway.filter;

import com.example.gateway.config.RoutesRoleConfig;
import com.example.gateway.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter, Ordered {

    private final JwtService jwtService;
    private final RoutesRoleConfig routesRoleConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Request received: {}", request.getURI());

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtService.extractAllClaims(token);
            String role = claims.get("role", String.class);
            String path = request.getURI().getPath();
            String method = request.getMethod().name();

            log.info("Decoded JWT claims: {}, Path: {}, Method: {}", claims, path, method);
            if (!isAccessAllowed(path, method, role)) {
                log.warn("Access denied for role {} on path {} with method {}", role, path, method);
                return onError(exchange, "Access denied", HttpStatus.FORBIDDEN);
            }

            return chain.filter(exchange);

        } catch (Exception e) {
            log.error("JWT decoding failed: {}", e.getMessage());
            return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        log.error("Error: {}", message);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, "text/plain");
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(message.getBytes())));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    // Check if access is allowed for the given specific endpoint, HTTP method, and user role
    private boolean isAccessAllowed(String path, String method, String userRole) {
        Map<String, List<String>> routeRoles = routesRoleConfig.getRoutes();

        // Normalize the path to match the configuration format
        String normalizedPath = path.replace("/", ".").replaceFirst("^\\.", "");
        String pathWithMethod = normalizedPath + "." + method.toUpperCase();
        log.info("Normalized path with method: {}", pathWithMethod);
        log.info("RouteRoles: {}", routeRoles);

        List<String> allowedRoles = routeRoles.get(pathWithMethod);
        if (allowedRoles == null) {
            log.warn("No roles defined for path: {}", path);
            return false;
        }

        return allowedRoles.contains(userRole);
    }


}
