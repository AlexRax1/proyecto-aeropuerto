package com.aeropuerto.usuarios.repository;

import com.aeropuerto.usuarios.model.RolUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolUserRepository
        extends JpaRepository<RolUser, Integer> {

    Optional<RolUser> findByNombreRol(String nombreRol);

}
