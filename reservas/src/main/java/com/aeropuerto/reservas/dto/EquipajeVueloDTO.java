package com.aeropuerto.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipajeVueloDTO {

    private String pasajero;

    private String maleta;

    private BigDecimal peso;
}