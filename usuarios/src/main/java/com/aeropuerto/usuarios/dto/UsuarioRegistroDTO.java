package com.aeropuerto.usuarios.dto;

import lombok.Data;

@Data
public class UsuarioRegistroDTO {
    // Datos de credenciales (para Auth)
    private String username;
    private String password;

    // Datos del usuario (para Usuarios)
    private String nombreCompleto;
    private String pasaporte;
    private String fechaNacimiento;
    private String nacionalidad;
    private String correo;
    private String telefono;
    private String direccion;
    private String codigoArea;
    private String numeroEmergencia;
    private String extensionNumeroEmergencia;
}
