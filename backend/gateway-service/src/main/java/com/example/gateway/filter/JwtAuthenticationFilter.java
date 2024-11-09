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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter, Ordered {

    private final JwtService jwtService;
    private final RoutesRoleConfig routesRoleConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();
        String requestMethod = request.getMethod().name();

        log.info("Request received - Path: {}, Method: {}", requestPath, requestMethod);

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtService.extractAllClaims(token);
            String userRole = claims.get("role", String.class);

            String normalizedPath = normalizePath(requestPath, requestMethod);
            log.debug("Normalized path: {}", normalizedPath);

            // TODO: Add RBAC Access accordingly
//            if (!isAccessAllowed(normalizedPath, userRole, routesRoleConfig.getRoutes())) {
//                log.warn("Access denied for role {} on path {}", userRole, normalizedPath);
//                return onError(exchange, "Access denied", HttpStatus.FORBIDDEN);
//            }

            return chain.filter(exchange);

        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage(), e);
            return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }

    private String normalizePath(String path, String method) {
        // Remove query parameters if present
        int queryIndex = path.indexOf('?');
        if (queryIndex != -1) {
            path = path.substring(0, queryIndex);
        }

        // Remove leading and trailing slashes and convert to dot notation
        String normalized = path.replaceAll("^/+|/+$", "")
                .replace("/", ".");

        return normalized + "." + method;
    }

    private boolean isAccessAllowed(String pathWithMethod, String userRole, Map<String, List<String>> routeRoles) {
        if (routeRoles == null || routeRoles.isEmpty()) {
            log.warn("No route roles configured");
            return false;
        }

        for (Map.Entry<String, List<String>> entry : routeRoles.entrySet()) {
            String routePattern = entry.getKey();
            List<String> allowedRoles = entry.getValue();

            // Convert the route pattern to handle both literal and placeholder IDs
            String regexPattern = routePattern
                    .replace(".", "\\.")
                    .replace("playerId", "\\d+")       // Match literal "playerId" with digits
                    .replace("tournamentId", "\\d+")   // Match literal "tournamentId" with digits
                    .replace("userId", "\\d+")   // Match literal "tournamentId" with digits
                    .replace("clanId", "\\d+")   // Match literal "tournamentId" with digits
                    .replace("clanUserId", "\\d+")
                    .replaceAll("\\{[^}]+\\}", "\\d+"); // Match {placeholder} syntax with digits

            log.debug("Comparing path '{}' with pattern '{}'", pathWithMethod, regexPattern);

            try {
                Pattern pattern = Pattern.compile("^" + regexPattern + "$");
                Matcher matcher = pattern.matcher(pathWithMethod);

                if (matcher.matches()) {
                    boolean isRoleAllowed = allowedRoles.contains(userRole);

                    log.info("Path '{}' matches pattern '{}'. Role {} is{} allowed",
                            pathWithMethod, routePattern, userRole, isRoleAllowed ? "" : " not");

                    return isRoleAllowed;
                }
            } catch (PatternSyntaxException e) {
                log.error("Invalid regex pattern for route: {} - Error: {}",
                        routePattern, e.getMessage());
            }
        }

        log.warn("No matching route pattern found for path: {}", pathWithMethod);
        return false;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, "text/plain");
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(message.getBytes()))
        );
    }

    @Override
    public int getOrder() {
        return -1;
    }
}