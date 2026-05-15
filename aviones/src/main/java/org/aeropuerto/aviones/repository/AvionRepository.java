package org.aeropuerto.aviones.repository;

import org.aeropuerto.aviones.model.Avion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvionRepository
        extends JpaRepository<Avion, Integer> {

    List<Avion> findByAerolineaId(
            Integer aerolineaId
    );

}
