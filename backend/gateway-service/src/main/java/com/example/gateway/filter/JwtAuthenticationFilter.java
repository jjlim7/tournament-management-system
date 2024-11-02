package com.example.gateway.filter;

import com.example.gateway.config.GatewayConfig;
import com.example.gateway.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;


//    @Value("${security.auth.url}")
//    private String authServiceBase;
//
//    @Value("${security.auth.introspect-api}")
//    private String authServiceIntrospect;

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//
//
//        WebClient webClient = WebClient.builder().baseUrl(authServiceBase).build();
//
//        return webClient
//                .get()
//                .uri(authServiceIntrospect)
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .flatMap(
//                        credentials -> {
//                            log.info("Starting authentication, ACCESS: {}", credentials);
//                            return chain.filter(exchange);
//                        })
//                .onErrorResume(
//                        ex -> onError(exchange, "Failed to authenticate token.", HttpStatus.UNAUTHORIZED));
//    }
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // Log the incoming request
//        ServerHttpRequest request = exchange.getRequest();
//        logger.info("Request received: {}", request.getURI());
//
//        // Bypass authentication and directly proceed to the next filter or target service
//        return chain.filter(exchange);
//    }
@Override
public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // Log the incoming request
    ServerHttpRequest request = exchange.getRequest();
    logger.info("Request received: {}", request.getURI());

    // Retrieve token from Authorization header
    String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
    }

    String token = authHeader.substring(7);
    try {
        // Decode the token
        Claims claims = jwtService.extractAllClaims(token);
        logger.info("Decoded JWT claims: {}", claims);

        // You can also add custom claim validation here if needed
        // For example: check roles or permissions

        // If token is valid, proceed with the request
        return chain.filter(exchange);

    } catch (Exception e) {
        logger.error("Failed to decode JWT: {}", e.getMessage());
        return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
    }
}

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        logger.error("ERROR ON CALL: {}", err);
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, "text/plain");
        return exchange
                .getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(err.getBytes())));
    }


    @Override
    public int getOrder() {
        return -1;
    }
}