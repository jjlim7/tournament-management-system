package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("Global Filter - Incoming Request: {} {}", request.getMethod(), request.getURI());
        return chain.filter(exchange)
                .doOnSuccess(unused -> log.info("Global Filter - Request Completed: {}", request.getURI()))
                .doOnError(error -> log.error("Global Filter - Request Failed: {}", error.getMessage()));
    }


    @Override
    public int getOrder() {
        // Set the order of this filter; HIGHEST_PRECEDENCE ensures it runs first
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
