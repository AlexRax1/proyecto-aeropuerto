package com.aeropuerto.operaciones.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles_tripulacion")
public class RolTripulacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rolId;

    @Column(name = "nombre_rol_tripulacion", length = 30)
    private String nombreRolTripulacion;
}
