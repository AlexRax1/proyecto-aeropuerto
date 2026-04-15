package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AerolineaRepository extends JpaRepository<Aerolinea, Long> {
}
