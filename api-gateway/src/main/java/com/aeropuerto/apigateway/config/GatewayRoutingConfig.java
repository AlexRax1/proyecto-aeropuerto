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

                // Usuarios
                .route("usuarios-service", r -> r
                        .path("/usuarios/**")
                        .uri("http://localhost:8082"))

                // Operaciones (¡Aquí agrupamos todos los endpoints de tu puerto 8083!)
                .route("operaciones-service", r -> r
                        .path(
                                "/aerolineas/**",
                                "/aeropuertos/**",
                                "/aviones/**",
                                "/consulta-aerolineas/**",
                                "/consulta-destinos/**",
                                "/api/operaciones/destinos/**",
                                "/tripulacion/**",
                                "/vuelos/**"
                        )
                        .uri("http://localhost:8083"))

                // Reservas
                .route("reservas-service", r -> r
                        .path("/api/reservas/**", "/equipajes/**")
                        .uri("http://localhost:8084"))

                .build();
    }
}