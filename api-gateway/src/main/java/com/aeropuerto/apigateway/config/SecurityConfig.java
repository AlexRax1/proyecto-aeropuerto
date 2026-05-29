package com.aeropuerto.apigateway.config;


import com.aeropuerto.apigateway.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors(Customizer.withDefaults()) // Toma la configuración de tu CorsConfig
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Seguridad y Usuarios
                        .pathMatchers(
                                "/auth/**",
                                "/usuarios/register"
                        ).permitAll()

                        // 3. Operaciones y Catálogos (Abiertos por completo con /** para ir rápido)
                        .pathMatchers(
                                "/aerolineas/**",
                                "/aeropuertos/**",
                                "/aviones/**",
                                "/consulta-aerolineas/**",
                                "/consulta-destinos/**",
                                "/api/operaciones/destinos/**",
                                "/vuelos/**"
                        ).permitAll()

                        // 4. Excepciones específicas de Reservas (Solo lo necesario para el mapa)
                        .pathMatchers(
                                "/api/reservas/vuelo/*/ocupados"
                        ).permitAll()


                        .anyExchange().authenticated()
                )
                // Inyectamos el filtro de JWT
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}