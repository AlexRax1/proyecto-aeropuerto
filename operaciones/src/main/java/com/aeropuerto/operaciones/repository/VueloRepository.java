package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VueloRepository  extends JpaRepository<Vuelo, Long> {

    @Query("SELECT v FROM Vuelo v WHERE v.origen.destinoId = :origenId " +
            "AND v.destino.destinoId = :destinoId " +
            "AND v.fechaSalida = :fechaSalida " +
            "AND v.estado = 'PROGRAMADO'")
    List<Vuelo> buscarVuelosDisponibles(
            @Param("origenId") Long origenId,
            @Param("destinoId") Long destinoId,
            @Param("fechaSalida") LocalDate fechaSalida
    );

    List<Vuelo> findByFechaSalidaBetween(
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );

    List<Vuelo> findByEstado(String estado);
}
