package com.example.gateway.filter;

import com.example.gateway.config.RouteConfig;
import com.example.gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    private final JwtService jwtService;
    private final RouteConfig routeConfig;

    public JwtAuthenticationFilter(JwtService jwtService, RouteConfig routeConfig) {
        this.jwtService = jwtService;
        this.routeConfig = routeConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();

        // Check if the request is for a public endpoint
        List<String> publicEndpoints = routeConfig.getPublicEndpoints();
        boolean isPublicEndpoint = publicEndpoints.stream()
                .anyMatch(requestPath::startsWith);

        if (isPublicEndpoint) {
            // Allow public endpoint requests to proceed without JWT validation
            return chain.filter(exchange);
        }

        // Check if the Authorization header is present
        List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
        if (authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header");
        }

        // Extract and validate the JWT token
        String token = authHeaders.get(0).substring(7); // Remove 'Bearer ' prefix
        try {
            jwtService.validateToken(token, jwtService.extractUsername(token));
        } catch (Exception e) {
            return onError(exchange, "Invalid or expired JWT token");
        }

        // Check if endpoint is admin
        boolean isAdminEndpoint = routeConfig.getPrivateAdminEndpoints().stream().anyMatch(requestPath::startsWith);
        if (isAdminEndpoint && !jwtService.validateAdminToken(token)) {
            return onError(exchange, "Token is expired or unauthorized");
        }

        // Proceed to the next filter in the chain
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Error", message);
        return response.setComplete();
    }
}
