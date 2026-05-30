package com.aeropuerto.operaciones.dto;

import lombok.Data;

@Data
public class ConsultaPasajerosVueloDTO {

    private String nombrePasajero;

    private String numeroPasaporte;

    private String nacionalidad;

    private Integer edad;

    private String telefono;

    private String correo;
}
