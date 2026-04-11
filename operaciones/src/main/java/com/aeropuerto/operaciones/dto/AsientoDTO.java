package com.aeropuerto.operaciones.dto;

import lombok.Data;

@Data
public class AsientoDTO {

    private Long idAsiento;
    private String fila;
    private String columna;
    private String categoria;
    private String tipo;
    private String estado;
}
