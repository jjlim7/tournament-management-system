package com.example.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "endpoints")  // Bind properties from application.yml
public class EndpointConfig {

    private final List<SecuredHost> securedEndpoints;

    public EndpointConfig(List<SecuredHost> securedEndpoints) {
        this.securedEndpoints = securedEndpoints;
    }

    public List<SecuredHost> getSecuredEndpoints() {
        return securedEndpoints;
    }

    public record SecuredHost(
            String host,
            List<SecuredPath> paths) {
    }

    public record SecuredPath(
            String path,
            String method,
            List<String> access) {
    }
}
