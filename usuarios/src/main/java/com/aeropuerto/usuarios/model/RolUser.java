package com.aeropuerto.usuarios.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rol_user")
@Data
public class RolUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_user_id")
    private Integer id;

    @Column(name = "nombre_rol")
    private String nombreRol;
}
