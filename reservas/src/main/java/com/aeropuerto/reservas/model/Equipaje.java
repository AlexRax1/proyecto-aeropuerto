package com.aeropuerto.reservas.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipaje")
@Data
public class Equipaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipaje_id")
    private Long equipajeId;

    // Relación simple (igual que usaste vuelo_id en Boleto)
    @Column(name = "boleto_id")
    private Long boletoId;

    private String maleta;

    private BigDecimal peso;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;// se puede para el tiempo de una vez ?

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        // Datos de auditoría para después
    }
}