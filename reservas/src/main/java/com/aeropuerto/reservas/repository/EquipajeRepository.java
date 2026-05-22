package com.aeropuerto.reservas.repository;

import com.aeropuerto.reservas.dto.EquipajeVueloDTO;
import com.aeropuerto.reservas.model.Equipaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface EquipajeRepository extends JpaRepository<Equipaje, Long> {

    @Query("""
    SELECT new com.aeropuerto.reservas.dto.EquipajeVueloDTO(
        e.nombreUsuario,
        e.maleta,
        e.peso
    )
    FROM Equipaje e, Boleto b
    WHERE e.boletoId = b.boletoId
    AND b.vueloId = :vueloId
""")
    List<EquipajeVueloDTO> findEquipajesByVueloId(
            @Param("vueloId") Integer vueloId
    );
}