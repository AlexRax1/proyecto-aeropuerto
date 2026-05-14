package com.aeropuerto.usuarios.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    private LocalDate fechaNacimiento;

    private String nacionalidad;

    @Column(unique = true)
    private String correo;

    private String telefono;

    private String direccion;

    @Column(name = "nombre")
    private String nombreCompleto;

    @Column(name = "num_pasaporte")
    private String pasaporte;

    @Column(name = "extension_telefonica")
    private String codigoArea;

    @Column(name = "num_emergencias")
    private String numeroEmergencia;



    //auditoria para despues
    @Column(name = "extension_num_emergencias")
    private String extensionNumeroEmergencia;

    @Column(name = "estado", length = 30)
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_creacion", length = 150)
    private String usuarioCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_modificacion", length = 150)
    private String usuarioModificacion;


}