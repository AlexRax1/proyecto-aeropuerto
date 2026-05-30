package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.PersonalTripulacion;
import com.aeropuerto.operaciones.model.RolTripulacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolTripulacionRepository  extends JpaRepository<RolTripulacion, Long> {
}
