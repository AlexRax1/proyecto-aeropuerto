package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.Avion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvionRepository
        extends JpaRepository<Avion, Integer> {

    List<Avion> findByAerolinea_AerolineaId(
            Integer aerolineaId
    );

    List<Avion> findByAerolineaAerolineaIdAndEstadoIgnoreCase(
            Integer aerolineaId,
            String estado
    );
}
