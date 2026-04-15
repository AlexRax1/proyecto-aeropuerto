package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.DestinoAeropuerto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinoAeropuertoRepository  extends JpaRepository<DestinoAeropuerto, Long> {
}
