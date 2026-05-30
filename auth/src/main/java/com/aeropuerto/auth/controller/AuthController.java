package com.aeropuerto.auth.controller;

import com.aeropuerto.auth.dto.*;
import com.aeropuerto.auth.model.Bitacora;
import com.aeropuerto.auth.model.Credencial;
import com.aeropuerto.auth.repository.BitacoraRepository;
import com.aeropuerto.auth.repository.CredencialRepository;
import com.aeropuerto.auth.model.RolUser;
import com.aeropuerto.auth.repository.RolUserRepository;
import com.aeropuerto.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CredencialRepository credencialRepository;

    @Autowired
    private RolUserRepository rolUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BitacoraRepository bitacoraRepository;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        
        if (credencialRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El username ya existe");
        }

        Credencial credencial = new Credencial();
        credencial.setUsername(request.getUsername());
        credencial.setPassword(passwordEncoder.encode(request.getPassword()));

        RolUser rolUsuario = rolUserRepository.findById(2).orElse(null);

        if (rolUsuario == null) {
            return ResponseEntity.badRequest().body("Rol USER no encontrado");
        }
        credencial.setRolUser(rolUsuario);

        //HACER EL LLENADO DE CREDENCIALES
        //credencial.setFechaCreacion(java.time.LocalDateTime.now());
        //credencial.setUsuarioCreacion("MS_USUARIOS");


        Credencial saved = credencialRepository.save(credencial);

        return ResponseEntity.ok(new RegisterResponse(saved.getUserId()));
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> rollbackCredencial(@PathVariable Integer userId) {
        credencialRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        // 1. Buscamos al usuario directamente por su ID
        Credencial usuario = credencialRepository.findById(request.getUserId()).orElse(null);

        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        // 2. Hasheamos la nueva contraseña (¡nunca en texto plano!)
        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 3. Guardamos los cambios
        credencialRepository.save(usuario);

        return ResponseEntity.ok().body("Contraseña restablecida exitosamente");
    }

    @PostMapping("/bitacora/registrar")
    public ResponseEntity<?> registrarBitacora(@RequestBody BitacoraDTO dto) {

        Bitacora bitacora = new Bitacora();

        // Si viene el userId, lo asociamos (asumiendo que tu entidad maneja la relación)
        if (dto.getUserId() != null) {
            Credencial usuario = credencialRepository.findById(dto.getUserId()).orElse(null);
            bitacora.setUsuario(usuario);
        }

        bitacora.setMicroservicioAfectado(dto.getMicroservicioAfectado());
        bitacora.setEndpoint(dto.getEndpoint());
        bitacora.setAccion(dto.getAccion());
        bitacora.setFechaHora(java.time.LocalDateTime.now());
        bitacora.setAccion(dto.getAccion());
        bitacora.setIdAfectado(dto.getIdAfectado());

        bitacoraRepository.save(bitacora);
        return ResponseEntity.ok().build();
    }
}