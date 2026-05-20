package com.aeropuerto.reservas.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestAbordajeDTO {
    private Integer idusuario;
    private Integer numMaletas;
    private Integer idVuelo;

}
