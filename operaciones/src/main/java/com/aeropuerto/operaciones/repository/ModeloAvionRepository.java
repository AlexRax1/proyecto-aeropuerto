package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.ModeloAvion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloAvionRepository extends JpaRepository<ModeloAvion, Long> {
}
