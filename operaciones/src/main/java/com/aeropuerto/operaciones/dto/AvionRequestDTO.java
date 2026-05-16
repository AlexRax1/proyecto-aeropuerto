package com.aeropuerto.operaciones.dto;


import lombok.Data;

@Data
public class AvionRequestDTO {

    //creacion de un avion (que se va a basar en modelos de aviones ya creados)

    //importante de momento
    private Long aerolineaId;
    private Long modeloAvionId;


    private String marca;
    private String ano;

    private Integer cantAsientosEconomica;
    private Integer cantAsientosEjecutiva;

    // Datos del Modelo a crear
    private String nombreModelo;
    private Integer cantFilas;
    private Integer cantColumnas;
    private String mapaColumnas;
}
