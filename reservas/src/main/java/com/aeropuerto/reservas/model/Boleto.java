package com.aeropuerto.reservas.model;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "boleto")
@Data
public class Boleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boleto_id")
    private Long boletoId;

    @Column(name = "vuelo_id")
    private Long vueloId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "asiento_id")
    private Long asientoId;

    private String estado;

    @Column(name = "cant_maletas")
    private Integer cantMaletas;

    @Column(name = "costo_boleto")
    private BigDecimal costoBoleto;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "codigo_asiento")
    private String codigoAsiento;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        // datos de auditoria para despues
    }

    @Column(name = "estado_abordaje")
    private String estadoAbordaje;

    @Column(name = "monto_extra_equipaje")
    private BigDecimal montoExtra;

    @Column(name = "cant_maletas_extra")
    private Integer cantMaletasExtra;
}