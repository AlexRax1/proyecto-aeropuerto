package com.aeropuerto.operaciones.dto;

import lombok.Data;

@Data
public class CrearPaqTripulacionDTO {

    private String nombrePaquete;

    private Integer pilotoId;

    private Integer copilotoId;

    private Integer ingenieroId;

    private Integer sobrecargo1Id;

    private Integer sobrecargo2Id;

    private Integer sobrecargo3Id;
}
