package com.aeropuerto.usuarios.repository;

import com.aeropuerto.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByPasaporte(String pasaporte);

    boolean existsByCorreo(String correo);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByPasaporte(String numPasaporte);
}