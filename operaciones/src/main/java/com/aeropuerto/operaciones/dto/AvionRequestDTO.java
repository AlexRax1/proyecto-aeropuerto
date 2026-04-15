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
    //despues
    // private Integer cantAsientosEconomica;
    // private Integer cantAsientosEjecutiva;
}
