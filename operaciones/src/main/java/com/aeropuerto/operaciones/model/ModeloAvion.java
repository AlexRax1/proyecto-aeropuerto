package com.aeropuerto.operaciones.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "modelo_avion")
@Data
public class ModeloAvion{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modelo_avion_id")
    private Long id;

    @Column(name = "modelo_avion_nombre", length = 100)
    private String nombre;

    @Column(name = "cant_filas")
    private Integer cantFilas;

    @Column(name = "cant_columnas")
    private Integer cantColumnas;

    @Column(name = "mapa_columnas", length = 50)
    private String mapaColumnas;
}
