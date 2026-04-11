package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.PersonalTripulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalTripulacionRepository  extends JpaRepository<PersonalTripulacion, Long> {
}
