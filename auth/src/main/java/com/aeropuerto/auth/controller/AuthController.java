package com.aeropuerto.auth.controller;


import com.aeropuerto.auth.dto.AuthResponse;
import com.aeropuerto.auth.dto.LoginRequest;
import com.aeropuerto.auth.model.Credencial;
import com.aeropuerto.auth.repository.CredencialRepository;
import com.aeropuerto.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CredencialRepository credencialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // se busca al usuairo
        Credencial usuario = credencialRepository.findByUsername(request.getUsername())
                .orElse(null);

        // si no lo encuentra o la contrasena coincide(con el hash)
        if (usuario == null || !passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        String token = jwtService.generarToken(usuario);

        // gurdados en bitacora

        return ResponseEntity.ok(new AuthResponse(token));
    }
}