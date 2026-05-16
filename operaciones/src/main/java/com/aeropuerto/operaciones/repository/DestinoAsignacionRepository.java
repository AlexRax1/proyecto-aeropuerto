package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.dto.ConsultaAerolineaDTO;
import com.aeropuerto.operaciones.model.DestinoAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinoAsignacionRepository
        extends JpaRepository<DestinoAsignacion, Integer> {

    @Query("""
    SELECT new com.aeropuerto.operaciones.dto.ConsultaAerolineaDTO(
        a.nombreAerolinea,
        a.cantAviones,
        d.ciudadDestino,
        d.paisDestino
    )
    FROM DestinoAsignacion da
    JOIN da.aerolinea a
    JOIN da.destino d
    WHERE d.nombreAeropuerto = :aeropuerto
""")
    List<ConsultaAerolineaDTO> consultarAerolineas(
            @Param("aeropuerto") String aeropuerto
    );

        @Query(value = """
        SELECT
            d.nombre_aeropuerto,
            d.pais_destino,
            d.ciudad_destino
        FROM aerolineas a
        INNER JOIN destino_asignacion da
            ON a.aerolinea_id = da.aerolinea_id
        INNER JOIN destinos_aeropuertos d
            ON da.destino_id = d.destino_id
        WHERE LOWER(a.nombre_aerolinea) =
              LOWER(:aerolinea)
        """, nativeQuery = true)

        List<Object[]> buscarDestinosPorAerolinea(
                @Param("aerolinea") String aerolinea
        );

    @Query(value = """
    SELECT nombre_aerolinea
    FROM aerolineas
    """, nativeQuery = true)

    List<String> obtenerAerolineas();
}
