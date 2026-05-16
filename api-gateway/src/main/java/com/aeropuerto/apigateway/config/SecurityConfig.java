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
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Permite el tráfico a tu servicio de autenticación para que puedas hacer login/registro
                        .pathMatchers("/auth/**").permitAll()

                        //pruebas
                        .pathMatchers("/aviones/**").permitAll()

                        //ya no permitira todo, solo lo hara antes de pagar ahi si te pedira login si o si
                        //.pathMatchers("/api/reservas/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/reservas/vuelo/*/ocupados").permitAll()
                        /*
                        // 2. Rutas públicas de Operaciones (Ejemplo: listar vuelos)
                        .pathMatchers(HttpMethod.GET, "/operaciones/vuelos/listar").permitAll()*/
                        // 2. Rutas públicas de Operaciones (Ejemplo: listar vuelos)
                        //.pathMatchers( "/aviones/**").permitAll()



                        /* agregar rutas que requieran de roles en especifico :
                        // Rutas que SOLO un ADMIN puede tocar (Ej: registrar un avión nuevo)
                        .pathMatchers(HttpMethod.POST, "/aviones/crear").hasAuthority("ADMIN")

                        // Rutas que un PILOTO o ADMIN pueden ver
                        .pathMatchers(HttpMethod.GET, "/aviones/rutas").hasAnyAuthority("ADMIN", "PILOTO")
                            */


                        // a todos los demas no declarados aca si les pedira token de autorizacion
                        .anyExchange().authenticated()
                ).addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}