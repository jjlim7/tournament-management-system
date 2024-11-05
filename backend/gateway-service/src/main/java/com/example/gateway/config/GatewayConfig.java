package com.example.gateway.config;

import com.example.gateway.config.constant.ConfigurationConstants;
import com.example.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${ms.elo-ranking-service.root}")
    private String msEloRankRoot;

    @Value("${ms.matchmaking-service.root}")
    private String msMatchmakingRoot;

    @Value("${ms.user-service.root}")
    private String msUserRoot;

    @Value("${ms.tournament-service.root}")
    private String msTournamentRoot;

    @Value("${ms.auth-service.root}")
    private String msAuthRoot;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        logger.info("Initializing Gateway Routes with ELO Rank Root: {}", msEloRankRoot);

        return routeLocatorBuilder.routes()
                // Auth Service Route (no JWT filter)
                .route(ConfigurationConstants.AUTH_SERVICE_ID, r -> r
                        .path("/auth/api/**")
                        .filters(f -> f.rewritePath("/auth/api/(?<segment>.*)", "/api/${segment}"))
                        .uri(msAuthRoot)
                )

                // ELO Ranking Service Route
                .route(ConfigurationConstants.ER_SERVICE_ID, r -> r
                        .path("/elo-ranking/api/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .rewritePath("/elo-ranking/api/(?<segment>.*)", "/api/${segment}")
                        )
                        .uri(msEloRankRoot)
                )

                // Matchmaking Service Route
                .route(ConfigurationConstants.MM_SERVICE_ID, r -> r
                        .path("/matchmaking/api/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .rewritePath("/matchmaking/api/(?<segment>.*)", "/api/${segment}")
                        )
                        .uri(msMatchmakingRoot)
                )

                // Tournament Service Route
                .route(ConfigurationConstants.T_SERVICE_ID, r -> r
                        .path("/tournament/api/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .rewritePath("/tournament/api/(?<segment>.*)", "/api/${segment}")
                        )
                        .uri(msTournamentRoot)
                )

                // User Service Route
                .route(ConfigurationConstants.U_SERVICE_ID, r -> r
                        .path("/clanusers/api/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .rewritePath("/userclan/api/(?<segment>.*)", "/${segment}")
                        )
                        .uri(msUserRoot)
                )
                .build();
    }

    /**
     * Configure CORS to allow requests from any origin with specific methods and headers.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
