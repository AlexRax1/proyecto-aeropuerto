package com.aeropuerto.reservas.dto;
import lombok.Data;
import java.util.List;

@Data
public class ValidacionChoqueHorarioDTO {
    private Long vueloNuevoId;
    private List<Long> vuelosExistentesIds;
}