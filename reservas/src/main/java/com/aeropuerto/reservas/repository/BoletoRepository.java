package com.aeropuerto.reservas.repository;

import com.aeropuerto.reservas.model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface BoletoRepository extends JpaRepository<Boleto, Long> {

    // Extrae una lista de números [1, 5, 12] para que Angular los pinte de rojo
    @Query("SELECT b.asientoId FROM Boleto b WHERE b.vueloId = :vueloId AND b.estado IN ('RESERVADO', 'PAGADO')")
    List<Long> findAsientosOcupadosByVueloId(@Param("vueloId") Long vueloId);


    Optional<Boleto> findByVueloIdAndUsuarioIdAndEstado(Integer vueloId, Integer usuarioId, String estado);

    @Modifying
    @Transactional
    @Query("UPDATE Boleto b SET b.estadoAbordaje = 'CANCELADO' WHERE b.vueloId = :vueloId AND b.estadoAbordaje = 'PENDIENTE'")
    int cancelarBoletosPendientes(@Param("vueloId") Integer vueloId);
}