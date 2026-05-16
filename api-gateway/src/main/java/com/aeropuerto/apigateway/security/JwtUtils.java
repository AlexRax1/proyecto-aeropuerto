package com.aeropuerto.apigateway.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    // Transforma tu texto secreto en una llave criptográfica real
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Intenta abrir el token y extraer su información (Claims)
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Valida si el token es correcto y no ha expirado
    public boolean isTokenValid(String token) {
        try {
            getAllClaimsFromToken(token); // Si logra extraer los datos, la firma es válida
            return true;
        } catch (Exception e) {
            // Si cae aquí, es porque la firma no coincide, el token expiró, o el texto no es un JWT
            System.out.println("Error al validar el token: " + e.getMessage());
            return false;
        }
    }
    //modificar
    public Integer getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId", Integer.class);
    }
    // Extrae el nombre de usuario (el "Subject" del token)

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // EN TU GATEWAY -> JwtUtils.java
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);

        // Lo extraemos como String (porque así lo envías desde el Auth: "ADMIN,USER")
        String rolesStr = claims.get("roles", String.class);

        if (rolesStr == null || rolesStr.trim().isEmpty()) {
            return List.of(); // Si no tiene roles, devolvemos lista vacía
        }

        // Lo cortamos por las comas y lo convertimos a Lista
        return java.util.Arrays.asList(rolesStr.split(","));
    }
}