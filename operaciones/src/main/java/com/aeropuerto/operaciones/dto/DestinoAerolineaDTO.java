package com.aeropuerto.operaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinoAerolineaDTO {

    private String nombreAeropuerto;
    private String paisDestino;
    private String ciudadDestino;

}
