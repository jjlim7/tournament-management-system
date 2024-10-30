package com.example.gateway.config;

import com.example.gateway.config.constant.ConfigurationConstants;
import com.example.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;

import java.util.List;
import java.util.function.Function;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Flux;

import static com.example.gateway.config.constant.ConfigurationConstants.API_V1;

@Slf4j
@Configuration
public class GatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    public GatewayConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Value("${ms.elo-ranking-service.root}")
    private String msEloRankRoot;

    @Value("${ms.matchmaking-service.root}")
    private String msMatchmakingRoot;

    @Value("${ms.user-service.root}")
    private String msUserRoot;

    @Value("${ms.tournament-service.root}")
    private String msTournamentRoot;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        logger.info("Initializing Gateway Routes with ELO Rank Root: {}", msEloRankRoot);

        RouteLocator routeLocator = routeLocatorBuilder.routes()
                // ELO Ranking Service Route
                .route(ConfigurationConstants.ER_SERVICE_ID, r -> r
                        .path("/elo-ranking/api/**")  // Incoming prefixed path
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .rewritePath("/elo-ranking/api/(?<segment>.*)", "/api/${segment}")  // Rewrite path
                        )
                        .uri(msEloRankRoot)  // Forward to ELO service
                )

                // Matchmaking Service Route
                .route(ConfigurationConstants.MM_SERVICE_ID, r -> r
                        .path("/matchmaking/api/**")  // Incoming prefixed path
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .rewritePath("/matchmaking/api/(?<segment>.*)", "/api/${segment}")
                        )
                        .uri(msMatchmakingRoot)  // Forward to Matchmaking service
                )

                // Tournament Service Route
                .route(ConfigurationConstants.T_SERVICE_ID, r -> r
                        .path("/tournament/api/**")  // Incoming prefixed path
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .rewritePath("/tournament/api/(?<segment>.*)", "/api/${segment}")
                        )
                        .uri(msTournamentRoot)  // Forward to Tournament service
                )

                // User Service Route
                .route(ConfigurationConstants.U_SERVICE_ID, r -> r
                        .path("/userclan/api/**")  // Incoming prefixed path
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .rewritePath("/userclan/api/(?<segment>.*)", "/${segment}")
                        )
                        .uri(msUserRoot)  // Forward to User service
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
    /**
     * Configure CORS to allow requests from any origin with specific methods and headers.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // Allow requests from any origin
        config.setAllowedOrigins(List.of("*"));
        // Allow specific HTTP methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow all headers
        config.setAllowedHeaders(List.of("*"));
        // Allow credentials if needed
        config.setAllowCredentials(true);
        // Register the CORS configuration for all paths
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
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
