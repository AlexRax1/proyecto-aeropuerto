package com.aeropuerto.operaciones.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "destino_asignacion")
public class DestinoAsignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asignacion_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;

    @ManyToOne
    @JoinColumn(name = "destino_id")
    private DestinoAeropuerto destino;
}