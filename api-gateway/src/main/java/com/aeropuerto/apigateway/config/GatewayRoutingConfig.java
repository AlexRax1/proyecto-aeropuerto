package com.aeropuerto.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutingConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("http://localhost:8081"))

                /*
                // agregar mas rutas
                .route("operaciones-service", r -> r
                        .path("/operaciones/**")
                        .uri("http://localhost:8082"))
                */

                // agregar mas rutas
                .route("operaciones-service", r -> r
                        .path("/aviones/**")
                        .uri("http://localhost:8083"))

                // agregar mas rutas
                .route("reservas-service", r -> r
                        .path("/api/reservas/**")
                        .uri("http://localhost:8084"))


                .build();
    }
}