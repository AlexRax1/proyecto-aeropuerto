package com.aeropuerto.operaciones.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ConsultaVueloDTO {

    private Integer numeroVuelo;

    private String modeloAvion;

    private String aerolinea;

    private String origen;

    private String destino;

    private LocalDate fechaSalida;

    private LocalTime horaSalida;

    private LocalDate fechaLlegada;

    private LocalTime horaLlegada;
}
