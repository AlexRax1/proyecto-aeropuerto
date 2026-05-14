package com.aeropuerto.usuarios.controller;

import com.aeropuerto.usuarios.dto.UsuarioRegistroDTO;
import com.aeropuerto.usuarios.model.Usuario;
import com.aeropuerto.usuarios.repository.UsuarioRepository;
import com.aeropuerto.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
@RequiredArgsConstructor
public class usuariosController {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioRegistroDTO dto) {

        // 1. Validaciones previas para no gastar llamadas de red en vano
        if (usuarioRepository.existsByPasaporte(dto.getPasaporte())) {
            return ResponseEntity.badRequest().body("El pasaporte ya está registrado");
        }
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        try {
            usuarioService.registrarUsuarioCompleto(dto);
            return ResponseEntity.ok("Usuario y credenciales registradas correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
