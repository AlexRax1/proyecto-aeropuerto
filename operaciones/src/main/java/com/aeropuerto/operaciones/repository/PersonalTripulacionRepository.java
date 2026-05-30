package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.model.PersonalTripulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PersonalTripulacionRepository  extends JpaRepository<PersonalTripulacion, Long> {

    @Query(value = """

SELECT COUNT(*) > 0
FROM vuelos v

JOIN asignacion_tripulacion at
ON at.vuelo_id = v.vuelo_id

WHERE at.tripulacion_id = :tripulacionId

AND v.estado <> 'CANCELADO'

AND (

    CAST(
        CONCAT(v.fecha_salida, ' ', v.hora_salida)
        AS TIMESTAMP
    ) <= :fechaLlegadaHora

    AND

    CAST(
        CONCAT(v.fecha_llegada, ' ', v.hora_llegada)
        AS TIMESTAMP
    ) >= :fechaSalidaHora
)

""", nativeQuery = true)
    boolean existeConflictoHorario(

            @Param("tripulacionId")
            Integer tripulacionId,

            @Param("fechaSalidaHora")
            LocalDateTime fechaSalidaHora,

            @Param("fechaLlegadaHora")
            LocalDateTime fechaLlegadaHora
    );

    List<PersonalTripulacion>
    findByRolNombreRolTripulacionAndEstadoIgnoreCase(
            String rol,
            String estado
    );
}
