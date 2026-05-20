package com.aeropuerto.usuarios.service;

import com.aeropuerto.usuarios.client.AuthClient;
import com.aeropuerto.usuarios.dto.AuthRegisterRequest;
import com.aeropuerto.usuarios.dto.AuthRegisterResponse;
import com.aeropuerto.usuarios.dto.UsuarioRegistroDTO;
import com.aeropuerto.usuarios.model.Usuario;
import com.aeropuerto.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AuthClient authClient;

    @Transactional // Si ocurre un error aquí, Spring revierte la BD local automáticamente
    public void registrarUsuarioCompleto(UsuarioRegistroDTO dto) {

        // 1. Preparar los datos para enviárselos a Auth
        AuthRegisterRequest authRequest = new AuthRegisterRequest();
        authRequest.setUsername(dto.getUsername());
        authRequest.setPassword(dto.getPassword());

        Integer userIdGenerado = null;

        try {
            // 2. Comunicarnos con Auth (Síncrono)
            ResponseEntity<AuthRegisterResponse> response = authClient.registrarCredenciales(authRequest);

            if (response.getBody() == null || response.getBody().getUserId() == null) {
                throw new RuntimeException("Auth no devolvió el ID del usuario");
            }

            userIdGenerado = response.getBody().getUserId();

            // 3. Crear nuestra entidad Usuario
            Usuario usuario = new Usuario();
            usuario.setUserId(userIdGenerado); // El ID que nos dio Auth
            usuario.setNombreCompleto(dto.getNombreCompleto());
            usuario.setPasaporte(dto.getPasaporte());
            usuario.setNacionalidad(dto.getNacionalidad());
            usuario.setCorreo(dto.getCorreo());
            usuario.setTelefono(dto.getTelefono());
            usuario.setCodigoArea(dto.getCodigoArea());
            usuario.setDireccion(dto.getDireccion());
            usuario.setNumeroEmergencia(dto.getNumeroEmergencia());
            usuario.setExtensionNumeroEmergencia(dto.getExtensionNumeroEmergencia());
            usuario.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
            usuario.setEstado("ACTIVO");
            usuario.setFechaCreacion(LocalDateTime.now());

            // 4. Guardar en nuestra base de datos de Usuarios
            usuarioRepository.save(usuario);

        } catch (Exception e) {
            // eliminacion por si el usuario existe ya en auth
            System.err.println("Error al guardar en Usuarios: " + e.getMessage());

            // Si Auth sí generó el ID, pero nosotros fallamos, le decimos a Auth que lo borre
            if (userIdGenerado != null) {
                try {
                    authClient.eliminarCredenciales(userIdGenerado);
                    System.out.println("Rollback exitoso: Credencial " + userIdGenerado + " eliminada de Auth.");
                } catch (Exception rollbackException) {
                    System.err.println("ALERTA CRÍTICA: Falló el rollback en Auth: " + rollbackException.getMessage());
                    // Aquí en un proyecto real se mandaría a una cola de mensajes o log de alertas.
                }
            }

            // Lanzamos la excepción para que el usuario reciba un error 500 o 400
            throw new RuntimeException("Error en el registro del usuario. Proceso cancelado.");
        }
    }


    public Usuario buscarPorPasaporte(String numPasaporte) {
        return usuarioRepository.findByPasaporte(numPasaporte).orElse(null);
    }
}