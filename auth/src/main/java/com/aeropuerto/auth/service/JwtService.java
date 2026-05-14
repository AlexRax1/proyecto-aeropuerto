package com.aeropuerto.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;



@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;


    public String generarToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Extraemos los roles del usuario y los guardamos en el payload del token
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("roles", roles);

        return crearToken(claims, userDetails.getUsername());
    }

    private String crearToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // El subject suele ser el username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Expiración: 30 minutos (1000 ms * 60 seg * 30 min)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getFirma(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getFirma() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}