package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.RolTripulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolTripulacionRepository  extends JpaRepository<RolTripulacion, Long> {
}
