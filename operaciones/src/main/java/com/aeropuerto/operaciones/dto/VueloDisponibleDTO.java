package com.aeropuerto.operaciones.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VueloDisponibleDTO {
    private Long numeroVuelo;
    private String modelo;    // "Airbus A320"
    private String origen;    // "Ciudad de Guatemala"
    private String destino;   // "Miami"
    private String salida;    // "2026-07-10 08:00"
    private String llegada;   // "2026-07-10 10:30"
    private String tiempo;    // "2h 30m"
    private String economica; // "$250.00"
    private String ejecutiva; // "$600.00"
}