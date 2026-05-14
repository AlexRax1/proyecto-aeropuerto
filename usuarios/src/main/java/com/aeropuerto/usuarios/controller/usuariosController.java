package com.aeropuerto.usuarios.controller;

import com.aeropuerto.auth.dto.AuthResponse;
import com.aeropuerto.auth.dto.LoginRequest;
import com.aeropuerto.usuarios.model.Usuario;
import com.aeropuerto.usuarios.repository.RolUserRepository;
import com.aeropuerto.usuarios.repository.UsuarioRepository;
import com.aeropuerto.usuarios.model.RolUser;
import com.aeropuerto.usuarios.security.JwtService;
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
@RequestMapping("/auth")
@CrossOrigin("*")
public class usuariosController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RolUserRepository rolUserRepository;

    // LOGIN

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        // BUSCAR USUARIO

        Usuario usuario =
                usuarioRepository
                        .findByUsername(
                                request.getUsername()
                        )
                        .orElse(null);

        // VALIDAR CREDENCIALES

        if (
                usuario == null ||
                        !passwordEncoder.matches(
                                request.getPassword(),
                                usuario.getPassword()
                        )
        ) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }

        // GENERAR TOKEN

        String token =
                jwtService.generarToken(usuario);

        return ResponseEntity.ok(
                new AuthResponse(token)
        );
    }

    // REGISTER

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Usuario usuario
    ) {

        // VALIDAR USERNAME

        if (
                usuarioRepository.existsByUsername(
                        usuario.getUsername()
                )
        ) {

            return ResponseEntity
                    .badRequest()
                    .body("El username ya existe");
        }

        // VALIDAR PASAPORTE

        if (
                usuarioRepository.existsByPasaporte(
                        usuario.getPasaporte()
                )
        ) {

            return ResponseEntity
                    .badRequest()
                    .body("El pasaporte ya existe");
        }

        // ENCRIPTAR PASSWORD

        usuario.setPassword(
                passwordEncoder.encode(
                        usuario.getPassword()
                )
        );

        // BUSCAR ROL USER

        RolUser rolUsuario =
                rolUserRepository
                        .findByNombreRol("USER")
                        .orElse(null);

        if (rolUsuario == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Rol USER no encontrado");
        }

        // ASIGNAR ROL

        usuario.setRol(rolUsuario);

        // GUARDAR

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(
                "Usuario registrado correctamente"
        );
    }
}
