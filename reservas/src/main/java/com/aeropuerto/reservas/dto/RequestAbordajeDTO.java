package com.aeropuerto.reservas.dto;

import com.aeropuerto.reservas.model.Equipaje;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RequestAbordajeDTO {
    private Integer idusuario;
    private Integer numMaletas;
    private Integer idVuelo;

    private String nombrePasajero;
    private List<Equipaje> maletas;

}
