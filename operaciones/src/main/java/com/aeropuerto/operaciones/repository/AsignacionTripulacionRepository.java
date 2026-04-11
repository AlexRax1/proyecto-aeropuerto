package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.AsignacionTripulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionTripulacionRepository  extends JpaRepository<AsignacionTripulacion, Long> {
}
