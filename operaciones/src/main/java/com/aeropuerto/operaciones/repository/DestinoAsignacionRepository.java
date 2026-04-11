package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.DestinoAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinoAsignacionRepository  extends JpaRepository<DestinoAsignacion, Long> {
}
