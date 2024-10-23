package com.example.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "endpoints")  // Bind properties with the "endpoints" prefix
public class RouteConfig {

    private String publicEndpoints;
    private String privateElorankEndpoints;
    private String privateUserclanEndpoints;
    private String privateMatchingEndpoints;
    private String privateTournamentEndpoints;
    private String privateAdminEndpoints;

    // Convert comma-separated strings into lists
    public List<String> getPublicEndpoints() {
        return Arrays.asList(publicEndpoints.split(","));
    }

    public List<String> getPrivateElorankEndpoints() {
        return Arrays.asList(privateElorankEndpoints.split(","));
    }

    public List<String> getPrivateUserclanEndpoints() {
        return Arrays.asList(privateUserclanEndpoints.split(","));
    }

    public List<String> getPrivateMatchingEndpoints() {
        return Arrays.asList(privateMatchingEndpoints.split(","));
    }

    public List<String> getPrivateTournamentEndpoints() {
        return Arrays.asList(privateTournamentEndpoints.split(","));
    }

    public List<String> getPrivateAdminEndpoints() {
        return Arrays.asList(privateAdminEndpoints.split(","));
    }

    // Setters for Spring to inject the property values
    public void setPublicEndpoints(String publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
    }

    public void setPrivateElorankEndpoints(String privateElorankEndpoints) {
        this.privateElorankEndpoints = privateElorankEndpoints;
    }

    public void setPrivateUserclanEndpoints(String privateUserclanEndpoints) {
        this.privateUserclanEndpoints = privateUserclanEndpoints;
    }

    public void setPrivateMatchingEndpoints(String privateMatchingEndpoints) {
        this.privateMatchingEndpoints = privateMatchingEndpoints;
    }

    public void setPrivateTournamentEndpoints(String privateTournamentEndpoints) {
        this.privateTournamentEndpoints = privateTournamentEndpoints;
    }

    public void setPrivateAdminEndpoints(String privateAdminEndpoints) {
        this.privateAdminEndpoints = privateAdminEndpoints;
    }
}
