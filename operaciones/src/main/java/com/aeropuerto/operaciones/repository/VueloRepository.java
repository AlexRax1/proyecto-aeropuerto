package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VueloRepository  extends JpaRepository<Vuelo, Long> {

    @Query("SELECT v FROM Vuelo v WHERE v.origen.destinoId = :origenId " +
            "AND v.destino.destinoId = :destinoId " +
            "AND v.fechaSalida = :fechaSalida " +
            "AND v.estado = 'PENDIENTE ABORDAR'")
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

    @Query(value = """

SELECT COUNT(*) > 0

FROM vuelos v

WHERE v.avion_id = :avionId

AND v.estado <> 'CANCELADO'

AND (

    (
        CAST(
            CONCAT(v.fecha_salida, ' ', v.hora_salida)
            AS TIMESTAMP
        )

        <= :llegada
    )

    AND

    (
        CAST(
            CONCAT(v.fecha_llegada, ' ', v.hora_llegada)
            AS TIMESTAMP
        )

        >= :salida
    )
)

""", nativeQuery = true)
    boolean existeConflictoHorario(

            @Param("avionId")
            Integer avionId,

            @Param("salida")
            LocalDateTime salida,

            @Param("llegada")
            LocalDateTime llegada
    );

    Optional<Vuelo> findByVueloId(Long vueloId);

    List<Vuelo> findByAvionAvionId(Integer avionId);
}
