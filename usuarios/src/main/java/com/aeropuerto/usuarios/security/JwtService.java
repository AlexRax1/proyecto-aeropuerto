package com.aeropuerto.usuarios.security;

import io.jsonwebtoken.Claims;
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

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    // GENERAR TOKEN

    public String generarToken(
            UserDetails userDetails
    ) {

        Map<String, Object> claims =
                new HashMap<>();

        // AGREGAR ROLES

        String roles =
                userDetails
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        claims.put("roles", roles);

        return crearToken(
                claims,
                userDetails.getUsername()
        );
    }

    // CREAR TOKEN

    private String crearToken(
            Map<String, Object> claims,
            String subject
    ) {

        return Jwts
                .builder()

                .setClaims(claims)

                .setSubject(subject)

                .setIssuedAt(
                        new Date(System.currentTimeMillis())
                )

                // 30 MINUTOS

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 30
                        )
                )

                .signWith(
                        getFirma(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    // OBTENER CLAVE

    private Key getFirma() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }

    // EXTRAER USERNAME

    public String extraerUsername(
            String token
    ) {

        return extraerClaim(
                token,
                Claims::getSubject
        );
    }

    // EXTRAER EXPIRACION

    public Date extraerExpiracion(
            String token
    ) {

        return extraerClaim(
                token,
                Claims::getExpiration
        );
    }

    // EXTRAER CLAIM

    public <T> T extraerClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        final Claims claims =
                extraerTodosLosClaims(token);

        return claimsResolver.apply(claims);
    }

    // EXTRAER CLAIMS

    private Claims extraerTodosLosClaims(
            String token
    ) {

        return Jwts
                .parserBuilder()

                .setSigningKey(getFirma())

                .build()

                .parseClaimsJws(token)

                .getBody();
    }

    // VALIDAR TOKEN

    public boolean validarToken(
            String token,
            UserDetails userDetails
    ) {

        final String username =
                extraerUsername(token);

        return (
                username.equals(
                        userDetails.getUsername()
                )
                        &&
                        !tokenExpirado(token)
        );
    }

    // VALIDAR EXPIRACION

    private boolean tokenExpirado(
            String token
    ) {

        return extraerExpiracion(token)
                .before(new Date());
    }
}
