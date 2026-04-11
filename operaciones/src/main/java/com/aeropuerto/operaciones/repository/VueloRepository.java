package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VueloRepository  extends JpaRepository<Vuelo, Long> {
}
