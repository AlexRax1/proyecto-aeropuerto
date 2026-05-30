package com.aeropuerto.operaciones.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CrearVueloDTO {

    private Integer avionId;

    private Integer origen;
    private Integer destino;

    private LocalDate fechaSalida;
    private LocalTime horaSalida;

    private LocalDate fechaLlegada;
    private LocalTime horaLlegada;

    private BigDecimal precioEconomica;
    private BigDecimal precioEjecutiva;

    private String usuario;

    private Integer tripulacionId;
}