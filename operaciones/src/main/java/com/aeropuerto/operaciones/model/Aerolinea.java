package com.aeropuerto.operaciones.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "aerolineas")
@Data
public class Aerolinea{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aerolinea_id")
    private Long id;

    @Column(name = "nombre_aerolinea", length = 100)
    private String nombre;

    @Column(name = "cant_aviones")
    private Integer cantAviones;
}
