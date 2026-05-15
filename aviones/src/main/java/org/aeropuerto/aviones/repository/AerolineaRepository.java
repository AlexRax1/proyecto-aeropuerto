package org.aeropuerto.aviones.repository;

import org.aeropuerto.aviones.model.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AerolineaRepository
        extends JpaRepository<Aerolinea, Integer> {
}
