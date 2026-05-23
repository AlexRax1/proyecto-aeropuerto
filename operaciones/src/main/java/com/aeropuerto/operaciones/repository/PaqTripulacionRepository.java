package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.PaqTripulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaqTripulacionRepository
        extends JpaRepository<PaqTripulacion, Integer> {
}
