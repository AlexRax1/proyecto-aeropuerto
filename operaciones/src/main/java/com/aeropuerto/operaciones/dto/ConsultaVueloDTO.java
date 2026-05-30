package com.aeropuerto.operaciones.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ConsultaVueloDTO {

    private Integer vueloId;

    private Integer numeroVuelo;

    private String modeloAvion;

    private String aerolinea;

    private String origen;

    private String destino;

    private LocalDate fechaSalida;

    private LocalTime horaSalida;

    private LocalDate fechaLlegada;

    private LocalTime horaLlegada;

    public ConsultaVueloDTO() {
    }

    public ConsultaVueloDTO(
            Integer vueloId,
            String modeloAvion,
            String aerolinea,
            String origen,
            String destino,
            LocalDate fechaSalida,
            LocalTime horaSalida,
            LocalDate fechaLlegada,
            LocalTime horaLlegada
    ) {
        this.vueloId = vueloId;
        this.modeloAvion = modeloAvion;
        this.aerolinea = aerolinea;
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.horaSalida = horaSalida;
        this.fechaLlegada = fechaLlegada;
        this.horaLlegada = horaLlegada;
    }
}
