package com.example.gateway.config;

import com.example.gateway.config.constant.ConfigurationConstants;
import com.example.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import java.util.function.Function;
import reactor.core.publisher.Flux;

import static com.example.gateway.config.constant.ConfigurationConstants.API_V1;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${ms.elo-ranking.root}")
    private String msEloRankRoot;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        logger.info("Initializing Gateway Routes with ELO Rank Root: {}", msEloRankRoot);

        RouteLocator routeLocator = routeLocatorBuilder.routes()
                .route(
                        ConfigurationConstants.ER_SERVICE_ID,
                        getRoute(ConfigurationConstants.ER_SERVICE_ROOT, msEloRankRoot)
                )
                .build();

        routeLocator.getRoutes().subscribe(route -> {
            logger.info("Loaded Route ID: {}", route.getId());
            logger.info("Route URI: {}", route.getUri());

            // Log all route predicates
            route.getMetadata().forEach((key, value) ->
                    logger.info("Metadata - Key: {}, Value: {}", key, value));

            // Since Route doesn't have getPredicates() directly, use the toString() for inspection.
            logger.info("Full Route Definition: {}", route);
        });


        return routeLocator;
    }

    private Function<PredicateSpec, Buildable<Route>> getRoute(String root, String uri) {
        logger.info("Configuring route: {} -> {}", root, uri);
        return r -> r.path(root.concat("/**"))
                .filters(filterSpec -> {
                    logger.info("Handling request for path: {}", root);
                    return getGatewayFilterSpec(filterSpec, root);
                })
                .uri(uri);
    }
    private GatewayFilterSpec getGatewayFilterSpec(GatewayFilterSpec f, String serviceUri) {
        logger.info("Applying Gateway Filter Spec for service URI: {}", serviceUri);
        return f.rewritePath(
                        serviceUri.concat("(?<segment>.*)"),
                        API_V1.concat(serviceUri).concat("${segment}")
                )
                .filter(jwtAuthenticationFilter);
    }
}
