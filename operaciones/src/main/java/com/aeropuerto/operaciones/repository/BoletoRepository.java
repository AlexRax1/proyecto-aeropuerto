package com.aeropuerto.operaciones.repository;

import com.aeropuerto.operaciones.dto.ConsultaPasajerosVueloDTO;
import com.aeropuerto.operaciones.model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletoRepository
        extends JpaRepository<Boleto, Long> {

    @Query(value = """

    SELECT
        u.nombre AS nombrePasajero,

        u.num_pasaporte AS numeroPasaporte,

        u.nacionalidad AS nacionalidad,

        EXTRACT(
            YEAR FROM AGE(
                u.fecha_nacimiento::date
            )
        ) AS edad,

        CONCAT(
            u.extension_telefonica,
            ' ',
            u.telefono
        ) AS telefono,

        u.correo AS correo

    FROM boleto b

    INNER JOIN usuarios u
        ON b.usuario_id = u.user_id

    WHERE b.vuelo_id = :vueloId

""", nativeQuery = true)
    List<Object[]> consultarPasajerosPorVuelo(
            @Param("vueloId") Long vueloId
    );
}
