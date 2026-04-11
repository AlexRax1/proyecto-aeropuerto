package com.aeropuerto.operaciones.dto;


import lombok.Data;

import java.util.List;

@Data
public class EstructuraAvionDTO {


    private Long idAvion;
    private Long idAerolinea;
    private Integer cantFilas;
    private Integer cantColumnas;
    private String mapaColumnas;

    // Esta es tu matriz: Una lista de filas, donde cada fila es una lista de asientos
    private List<List<AsientoDTO>> matrizAsientos;

}

