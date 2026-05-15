package com.aeropuerto.operaciones.service;


import com.aeropuerto.operaciones.dto.VueloDisponibleDTO;
import com.aeropuerto.operaciones.model.Vuelo;
import com.aeropuerto.operaciones.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VueloService {

    private final VueloRepository vueloRepository;

    public List<VueloDisponibleDTO> buscarVuelos(Long origenId, Long destinoId, LocalDate fechaSalida) {
        List<Vuelo> vuelos = vueloRepository.buscarVuelosDisponibles(origenId, destinoId, fechaSalida);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return vuelos.stream().map(vuelo -> {
            // Unificar fecha y hora para cálculos y formato
            LocalDateTime fechaHoraSalida = LocalDateTime.of(vuelo.getFechaSalida(), vuelo.getHoraSalida());
            LocalDateTime fechaHoraLlegada = LocalDateTime.of(vuelo.getFechaLlegada(), vuelo.getHoraLlegada());

            // Calcular duración
            Duration duracion = Duration.between(fechaHoraSalida, fechaHoraLlegada);
            String tiempoVuelo = duracion.toHours() + "h " + duracion.toMinutesPart() + "m";

            return VueloDisponibleDTO.builder()
                    .numeroVuelo(vuelo.getVueloId())
                    .modelo(vuelo.getAvion().getModeloAvion().getNombre())
                    .origen(vuelo.getOrigen().getCiudadDestino())
                    .destino(vuelo.getDestino().getCiudadDestino())
                    .salida(fechaHoraSalida.format(formatter))
                    .llegada(fechaHoraLlegada.format(formatter))
                    .tiempo(tiempoVuelo)
                    .economica("$" + vuelo.getPrecioClaseEconomica())
                    .ejecutiva("$" + vuelo.getPrecioClaseEjecutiva())
                    .build();
        }).collect(Collectors.toList());
    }
}
