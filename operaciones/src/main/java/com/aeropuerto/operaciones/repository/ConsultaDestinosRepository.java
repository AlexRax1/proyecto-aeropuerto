package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.dto.DestinoAerolineaDTO;
import com.aeropuerto.operaciones.model.DestinoAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaDestinosRepository
        extends JpaRepository<DestinoAsignacion, Integer> {

    @Query(value = """
        SELECT
            a.nombre_aerolinea AS nombreAerolinea,
            d.nombre_aeropuerto AS nombreAeropuerto,
            d.pais_destino AS paisDestino,
            d.ciudad_destino AS ciudadDestino
        FROM aerolineas a
        INNER JOIN destino_asignacion da
            ON a.aerolinea_id = da.aerolinea_id
        INNER JOIN destinos_aeropuertos d
            ON da.destino_id = d.destino_id
        WHERE a.nombre_aerolinea = :nombreAerolinea
        """, nativeQuery = true)
    List<DestinoAerolineaDTO> consultarDestinos(
            @Param("nombreAerolinea") String nombreAerolinea
    );
}
