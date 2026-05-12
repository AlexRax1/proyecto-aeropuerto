package com.aeropuerto.reservas.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RedisTestController {
    // Esta es la herramienta principal para interactuar con Redis
    private final StringRedisTemplate redisTemplate;

    public RedisTestController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/test-redis")
    public String probarRedis() {
        // Escribimos algo en Redis
        redisTemplate.opsForValue().set("mi_prueba", "¡Hola desde Redis!");

        // Lo leemos
        String valor = redisTemplate.opsForValue().get("mi_prueba");

        return "Conexión exitosa, el valor es: " + valor;
    }
}