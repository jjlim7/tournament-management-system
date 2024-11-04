package com.example.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "route-roles")
public class RoutesRoleConfig {
    private Map<String, List<String>> routes; // Flattened map of "path.method" -> roles
}