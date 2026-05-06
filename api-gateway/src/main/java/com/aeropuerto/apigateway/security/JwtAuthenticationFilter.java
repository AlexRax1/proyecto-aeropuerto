package com.aeropuerto.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements WebFilter {


 
    private final JwtUtils jwtUtils;

    // Inyectamos nuestra herramienta JwtUtils
    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    /*

    @Autowired
    private JwtUtils jwtUtils;
    /*/
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 1. Si la ruta es pública (como login), dejamos que pase sin revisar token
        if (path.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

        // 2. Buscamos la cabecera "Authorization"
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 3. Verificamos que traiga el formato correcto: "Bearer eyJhbGci..."
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Cortamos la palabra "Bearer "

            // 4. Validamos la firma del token
            if (jwtUtils.isTokenValid(token)) {

                // Extraemos los datos
                String username = jwtUtils.getUsernameFromToken(token);
                List<String> roles = jwtUtils.getRolesFromToken(token);

                // Convertimos la lista de textos (ej. "ROLE_ADMIN") al formato que Spring Security entiende
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // 5. Creamos la "Credencial" oficial de Spring
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );

                // 6. Dejamos que la petición siga su curso, pero ahora le pegamos la credencial al contexto
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
            }
        }

        // Si llega aquí es porque no traía token o era inválido.
        // Simplemente continúa, y el SecurityConfig lo bloqueará con un 401 Unauthorized.
        return chain.filter(exchange);
    }
}