package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

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
