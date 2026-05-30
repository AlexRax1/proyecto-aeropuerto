package com.aeropuerto.apigateway.auditoria;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class BitacoraAuditFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;

    public BitacoraAuditFilter(WebClient.Builder webClientBuilder) {
        // Apuntamos al microservicio de Auth (Puerto 8081)
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. Dejamos que la petición fluya hacia su destino original
        return chain.filter(exchange).then(Mono.defer(() -> {

            HttpStatus status = (HttpStatus) exchange.getResponse().getStatusCode();

            // 2. Solo registramos si la operación fue exitosa (200 OK o 201 Created)
            if (status != null && status.is2xxSuccessful()) {

                String idAfectadoResponse = exchange.getResponse().getHeaders().getFirst("X-Afectado-Id");

                String path = exchange.getRequest().getURI().getPath();
                String method = exchange.getRequest().getMethod().name();
                String accion = null;
                String microservicio = null;
                String idAfectado = null;

// 3. Mapeamos acciones
                if (method.equals("PUT") && path.contains("/api/reservas/abordar")) {
                    accion = "Abordaje de pasajero";
                    microservicio = "RESERVAS";
                    idAfectado = idAfectadoResponse; // Viene del Header
                }
                else if (method.equals("PUT") && path.matches("/vuelos/.*/estado-abordado")) {
                    String idVuelo = path.split("/")[2];
                    accion = "Finalización de abordaje de avión";
                    microservicio = "OPERACIONES";
                    idAfectado = idVuelo; // Este es tan fácil que lo sacamos de la URL directamente
                }
                else if (method.equals("POST") && path.contains("/tripulacion/paquete")) {
                    accion = "Creación de tripulación";
                    microservicio = "OPERACIONES";
                    idAfectado = idAfectadoResponse; // Viene del Header
                }
                else if (method.equals("POST") && path.equals("/vuelos")) {
                    accion = "Creación de vuelo";
                    microservicio = "OPERACIONES";
                    idAfectado = idAfectadoResponse; // Viene del Header
                }

// 4. Disparamos a Auth
                if (accion != null) {
                    String userIdStr = exchange.getRequest().getHeaders().getFirst("X-User-Id");
                    Integer userId = (userIdStr != null && !userIdStr.equals("null")) ? Integer.parseInt(userIdStr) : null;

                    Map<String, Object> body = new HashMap<>();
                    body.put("userId", userId);
                    body.put("microservicioAfectado", microservicio);
                    body.put("endpoint", path);
                    body.put("accion", accion);
                    body.put("idAfectado", idAfectado);

                    // Envío asíncrono hacia Auth (no bloquea la respuesta hacia Angular)
                    return webClient.post()
                            .uri("/auth/bitacora/registrar")
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .onErrorResume(e -> {
                                // Si Auth está caído, registramos el error sin interrumpir el Gateway
                                System.err.println("Falló el guardado en bitácora: " + e.getMessage());
                                return Mono.empty();
                            });
                }
            }
            return Mono.empty();
        }));
    }

    // Usamos la prioridad más baja para asegurar que se ejecute DESPUÉS de que el token fue validado
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}