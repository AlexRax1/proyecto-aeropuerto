package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AsientoRepository  extends JpaRepository<Asiento, Long> {

    // asientos de un avion especificado por su id
    List<Asiento> findByAvionId(Long avionId);
}
