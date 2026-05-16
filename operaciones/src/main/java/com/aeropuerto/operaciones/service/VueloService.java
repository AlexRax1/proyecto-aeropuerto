package com.aeropuerto.operaciones.service;

import com.aeropuerto.operaciones.dto.ConsultaVueloDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VueloService {

    private final VueloRepository vueloRepository;
    private final AvionService avionService;

    // =====================================================
    // BUSCAR VUELOS DISPONIBLES
    // =====================================================

    public List<VueloDisponibleDTO> buscarVuelos(
            Long origenId,
            Long destinoId,
            LocalDate fechaSalida
    ) {

        List<Vuelo> vuelos =
                vueloRepository.buscarVuelosDisponibles(
                        origenId,
                        destinoId,
                        fechaSalida
                );

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm"
                );

        return vuelos.stream().map(vuelo -> {

            // Unificar fecha y hora
            LocalDateTime fechaHoraSalida =
                    LocalDateTime.of(
                            vuelo.getFechaSalida(),
                            vuelo.getHoraSalida()
                    );

            LocalDateTime fechaHoraLlegada =
                    LocalDateTime.of(
                            vuelo.getFechaLlegada(),
                            vuelo.getHoraLlegada()
                    );

            // Duración
            Duration duracion =
                    Duration.between(
                            fechaHoraSalida,
                            fechaHoraLlegada
                    );

            String tiempoVuelo =
                    duracion.toHours()
                            + "h "
                            + duracion.toMinutesPart()
                            + "m";

            return VueloDisponibleDTO.builder()

                    .numeroVuelo(
                            vuelo.getVueloId()
                    )

                    .modelo(
                            vuelo.getAvion()
                                    .getModeloAvion()
                                    .getNombre()
                    )

                    .origen(
                            vuelo.getOrigen()
                                    .getCiudadDestino()
                    )

                    .destino(
                            vuelo.getDestino()
                                    .getCiudadDestino()
                    )

                    .salida(
                            fechaHoraSalida.format(
                                    formatter
                            )
                    )

                    .llegada(
                            fechaHoraLlegada.format(
                                    formatter
                            )
                    )

                    .tiempo(
                            tiempoVuelo
                    )

                    .economica(
                            "$"
                                    + vuelo.getPrecioClaseEconomica()
                    )

                    .ejecutiva(
                            "$"
                                    + vuelo.getPrecioClaseEjecutiva()
                    )

                    .build();

        }).collect(Collectors.toList());
    }

    // =====================================================
    // MATRIZ DE ASIENTOS
    // =====================================================

    public EstructuraAvionDTO obtenerMatrizPorVuelo(
            Long vueloId
    ) {

        Vuelo vuelo =
                vueloRepository.findById(vueloId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Vuelo no encontrado con ID: "
                                                + vueloId
                                ));

        Integer avionId =
                vuelo.getAvion()
                        .getAvionId();

        return avionService
                .obtenerEstructuraAsientos(
                        avionId
                );
    }

    // =====================================================
    // CONSULTA DE VUELOS
    // =====================================================

    public List<ConsultaVueloDTO> consultarVuelos(
            LocalDate fechaDesde,
            LocalDate fechaHasta
    ) {

        List<Vuelo> vuelos =
                vueloRepository.findByFechaSalidaBetween(
                        fechaDesde,
                        fechaHasta
                );

        List<ConsultaVueloDTO> respuesta =
                new ArrayList<>();

        for (Vuelo vuelo : vuelos) {

            ConsultaVueloDTO dto =
                    new ConsultaVueloDTO();

            dto.setNumeroVuelo(
                    vuelo.getVueloId().intValue()
            );

            dto.setModeloAvion(
                    vuelo.getAvion()
                            .getModeloAvion()
                            .getNombre()
            );

            dto.setAerolinea(
                    vuelo.getAvion()
                            .getAerolinea()
                            .getNombreAerolinea()
            );

            dto.setOrigen(
                    vuelo.getOrigen()
                            .getCiudadDestino()
            );

            dto.setDestino(
                    vuelo.getDestino()
                            .getCiudadDestino()
            );

            dto.setFechaSalida(
                    vuelo.getFechaSalida()
            );

            dto.setHoraSalida(
                    vuelo.getHoraSalida()
            );

            dto.setFechaLlegada(
                    vuelo.getFechaLlegada()
            );

            dto.setHoraLlegada(
                    vuelo.getHoraLlegada()
            );

            respuesta.add(dto);
        }

        return respuesta;
    }
}