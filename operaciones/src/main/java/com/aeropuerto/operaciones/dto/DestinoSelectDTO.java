package com.aeropuerto.operaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DestinoSelectDTO {
    private Long id;
    private String labelCompleto;
}