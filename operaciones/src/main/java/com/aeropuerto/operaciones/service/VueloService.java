package com.aeropuerto.operaciones.service;


import com.aeropuerto.operaciones.dto.EstructuraAvionDTO;
import com.aeropuerto.operaciones.dto.VueloDisponibleDTO;
import com.aeropuerto.operaciones.model.Vuelo;
import com.aeropuerto.operaciones.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final AvionService avionService;

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


    public EstructuraAvionDTO obtenerMatrizPorVuelo(Long vueloId) {
        Vuelo vuelo = vueloRepository.findById(vueloId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vuelo no encontrado con ID: " + vueloId
                ));

        Long avionId = vuelo.getAvion().getId();
        return avionService.obtenerEstructuraAsientos(avionId);
    }
}
