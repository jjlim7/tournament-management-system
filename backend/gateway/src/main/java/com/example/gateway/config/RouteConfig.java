package com.example.gateway.config;

import com.example.gateway.filter.JwtAuthenticationFilter;
import com.example.gateway.filter.RoleAuthorizationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final EndpointConfig endpointConfig;

    public RouteConfig(JwtAuthenticationFilter jwtAuthenticationFilter, EndpointConfig endpointConfig) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.endpointConfig = endpointConfig;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        // Register secured routes with method-based RBAC checks
        endpointConfig.getSecuredEndpoints().forEach(host ->
                host.paths().forEach(path ->
                        routes.route(r -> r.path(path.path())
                                .and()
                                .method(path.method())
                                .filters(f -> f.filter(jwtAuthenticationFilter)  // Apply JWT filter
                                        .filter(new RoleAuthorizationFilter(path.access())))  // Apply Role filter
                                .uri("http://" + host.host()))));

        return routes.build();
    }

}
