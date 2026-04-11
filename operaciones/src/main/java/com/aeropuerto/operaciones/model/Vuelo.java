package com.aeropuerto.operaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;



@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vuelos")
public class Vuelo extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vuelo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avion_id")
    private Avion avion;

    @ManyToOne
    @JoinColumn(name = "origen")
    private DestinoAeropuerto origen;

    @ManyToOne
    @JoinColumn(name = "destino")
    private DestinoAeropuerto destino;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    @Column(name = "fecha_llegada")
    private LocalDate fechaLlegada;

    @Column(name = "hora_llegada")
    private LocalTime horaLlegada;

    @Column(length = 50)
    private String estado;

    @Column(name = "precio_clase_economica")
    private BigDecimal precioClaseEconomica;

    @Column(name = "precio_clase_ejecutiva")
    private BigDecimal precioClaseEjecutiva;

    @Column(name = "monto_extra_ventana")
    private BigDecimal montoExtraVentana;

    @Column(name = "montro_extra_pasillo")
    private BigDecimal montoExtraPasillo;

    @Column(name = "asientos_disponibles")
    private Integer asientosDisponibles;
}
