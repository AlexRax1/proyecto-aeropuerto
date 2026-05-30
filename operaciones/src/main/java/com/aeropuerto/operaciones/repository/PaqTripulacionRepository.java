package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.PaqTripulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaqTripulacionRepository
        extends JpaRepository<PaqTripulacion, Integer> {

    List<PaqTripulacion> findByEstado(String estado);
}
