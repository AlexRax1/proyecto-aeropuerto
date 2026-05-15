package org.aeropuerto.aviones.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "aviones")
@Data
public class Avion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avion_id")
    private Integer avionId;

    @Column(name = "aerolinea_id")
    private Integer aerolineaId;

    @Column(name = "modelo_avion_id")
    private Integer modeloAvionId;

    private String marca;

    @Column(name = "ano")
    private String ano;

    @Column(name = "cant_asientos_economica")
    private Integer cantAsientosEconomica;

    @Column(name = "cant_asientos_ejecutiva")
    private Integer cantAsientosEjecutiva;

    @Column(name = "cant_vuelos")
    private Integer cantVuelos;

    private String estado;
}
