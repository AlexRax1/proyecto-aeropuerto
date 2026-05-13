package com.aeropuerto.reservas.controller;



import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ReservaRedisService {

    private final StringRedisTemplate redisTemplate;

    public ReservaRedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Pone el candado. Retorna TRUE si logró bloquearlo, FALSE si alguien más ya lo tiene.
    public boolean bloquearAsiento(Long vueloId, Long asientoId, Long usuarioId) {
        String key = "vuelo:" + vueloId + ":asiento:" + asientoId;
        String value = String.valueOf(usuarioId);

        Boolean exito = redisTemplate.opsForValue()
                .setIfAbsent(key, value, Duration.ofMinutes(5));

        return Boolean.TRUE.equals(exito);
    }

    // Quita el candado una vez que se pagó oficialmente
    public void liberarAsiento(Long vueloId, Long asientoId) {
        String key = "vuelo:" + vueloId + ":asiento:" + asientoId;
        redisTemplate.delete(key);
    }

    public List<Long> obtenerAsientosBloqueados(Long vueloId) {
        Set<String> keys = redisTemplate.keys("vuelo:" + vueloId + ":asiento:*");
        List<Long> bloqueados = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {
                // Extrae el ID (42) de la llave "vuelo:1:asiento:42"
                String[] partes = key.split(":");
                bloqueados.add(Long.parseLong(partes[3]));
            }
        }
        return bloqueados;
    }
}