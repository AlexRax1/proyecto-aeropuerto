package com.aeropuerto.operaciones.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "personal_tripulacion")
public class PersonalTripulacion extends Auditoria{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_tripulacion_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private RolTripulacion rol;

    @Column(length = 50)
    private String nombre;

    @Column(length = 30)
    private String estado;
}
