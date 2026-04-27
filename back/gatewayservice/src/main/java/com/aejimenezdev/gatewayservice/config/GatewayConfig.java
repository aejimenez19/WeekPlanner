package com.aejimenezdev.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${app.services.auth-url}")
    private String authServiceUrl;

    @Value("${app.services.task-url}")
    private String taskServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri(authServiceUrl))
                .route("task-service", r -> r
                        .path("/api/tasks/**")
                        .uri(taskServiceUrl))
                .build();
    }
}