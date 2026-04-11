package com.aeropuerto.operaciones.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "asignacion_tripulacion")
public class AsignacionTripulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asignacion_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    private Vuelo vuelo;

    @ManyToOne
    @JoinColumn(name = "tripulacion_id")
    private PersonalTripulacion tripulacion;
}
