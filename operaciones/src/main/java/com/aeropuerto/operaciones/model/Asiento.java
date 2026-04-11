package com.aeropuerto.operaciones.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "asientos")
@Data
public class Asiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asiento_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avion_id")
    private Avion avion;

    @Column(name = "fila_asiento", length = 10)
    private String fila;

    @Column(name = "columna_asiento", length = 10)
    private String columna;

    @Column(name = "categoria_asiento", length = 20)
    private String categoria;

    @Column(name = "tipo_asiento", length = 30)
    private String tipo;

    @Column(length = 30)
    private String estado;
}
