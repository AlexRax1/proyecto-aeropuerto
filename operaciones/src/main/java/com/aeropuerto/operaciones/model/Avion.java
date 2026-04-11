package com.aeropuerto.operaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "aviones")
public class Avion extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avion_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;

    @ManyToOne
    @JoinColumn(name = "modelo_avion_id")
    private ModeloAvion modeloAvion;

    @Column(length = 100)
    private String marca;

    @Column(length = 4)
    private String ano;

    @Column(name = "cant_asientos_economica")
    private Integer cantAsientosEconomica;

    @Column(name = "cant_asientos_ejecutiva")
    private Integer cantAsientosEjecutiva;

    @Column(name = "cant_vuelos")
    private Integer cantVuelos;

    @Column(length = 30)
    private String estado;


}


