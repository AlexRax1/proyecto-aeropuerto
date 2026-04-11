package com.aeropuerto.operaciones.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "destinos_aeropuertos")
@Data
public class DestinoAeropuerto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destino_id")
    private Long id;

    @Column(name = "ciudad_destino", length = 100)
    private String ciudadDestino;

    @Column(name = "pais_destino", length = 100)
    private String paisDestino;

    @Column(name = "nombre_aeropuerto", length = 100)
    private String nombreAeropuerto;
}
