package com.aeropuerto.reservas.repository;

import com.aeropuerto.reservas.model.Equipaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipajeRepository extends JpaRepository<Equipaje, Long> {
}