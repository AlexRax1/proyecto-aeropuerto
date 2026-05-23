package com.aeropuerto.operaciones.service;

import com.aeropuerto.operaciones.dto.ConsultaVueloDTO;
import com.aeropuerto.operaciones.dto.EstructuraAvionDTO;
import com.aeropuerto.operaciones.dto.ValidacionChoqueHorarioDTO;
import com.aeropuerto.operaciones.dto.VueloDisponibleDTO;
import com.aeropuerto.operaciones.dto.*;
import com.aeropuerto.operaciones.model.Avion;
import com.aeropuerto.operaciones.model.DestinoAeropuerto;
import com.aeropuerto.operaciones.model.PaqTripulacion;
import com.aeropuerto.operaciones.model.Vuelo;
import com.aeropuerto.operaciones.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VueloService {

    private final VueloRepository vueloRepository;
    private final AvionRepository avionRepository;
    private final DestinoAeropuertoRepository destinoRepository;
    private final AvionService avionService;
    private final BoletoRepository boletoRepository;
    private final PaqTripulacionRepository tripulacionPaqueteRepository;

    // CREAR VUELO
    @Transactional
    public Vuelo crearVuelo(
            CrearVueloDTO dto
    ) {

        // VALIDAR AVIÓN
        Avion avion =
                avionRepository.findById(
                        dto.getAvionId()
                ).orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Avión no encontrado"
                        )
                );

        // FA02
        if (!"Activo".equalsIgnoreCase(
                avion.getEstado()
        )) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede crear un vuelo porque el avión no está activo."
            );
        }

        // VALIDAR AEROPUERTOS
        if (
                dto.getOrigen().equals(
                        dto.getDestino()
                )
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede seleccionar el mismo aeropuerto de salida y llegada."
            );
        }

        DestinoAeropuerto origen =
                destinoRepository.findById(
                        Long.valueOf(dto.getOrigen())
                ).orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Aeropuerto origen no encontrado"
                        )
                );

        DestinoAeropuerto destino =
                destinoRepository.findById(
                        Long.valueOf(dto.getDestino())
                ).orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Aeropuerto destino no encontrado"
                        )
                );

        // VALIDAR FECHAS
        LocalDateTime salida =
                LocalDateTime.of(
                        dto.getFechaSalida(),
                        dto.getHoraSalida()
                );

        LocalDateTime llegada =
                LocalDateTime.of(
                        dto.getFechaLlegada(),
                        dto.getHoraLlegada()
                );

        // FA07
        if (!llegada.isAfter(salida)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha y hora de llegada debe ser mayor a la fecha y hora de salida."
            );
        }

        // FA05
        long horas =
                Duration.between(
                        LocalDateTime.now(),
                        salida
                ).toHours();

        if (horas < 5) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tiempo mínimo para la preparación 5 horas a partir de la hora actual."
            );
        }

        // VALIDAR PRECIOS
        if (
                dto.getPrecioEconomica() == null
                        ||
                        dto.getPrecioEconomica()
                                .compareTo(BigDecimal.ZERO) <= 0
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Precio de clase económica inválido."
            );
        }

        if (
                dto.getPrecioEjecutiva() == null
                        ||
                        dto.getPrecioEjecutiva()
                                .compareTo(BigDecimal.ZERO) <= 0
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Precio de clase ejecutiva inválido."
            );
        }

        // VALIDAR CONFLICTO DE HORARIOS
        boolean conflicto =
                vueloRepository.existeConflictoHorario(
                        avion.getAvionId(),
                        salida,
                        llegada
                );

        if (conflicto) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El avión ya tiene un vuelo programado en ese horario."
            );
        }

        // VALIDAR TRIPULACIÓN
        if (dto.getTripulacionId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe seleccionar tripulación."
            );
        }

        // CREAR VUELO
        Vuelo vuelo = new Vuelo();

        vuelo.setAvion(avion);

        vuelo.setOrigen(origen);

        vuelo.setDestino(destino);

        vuelo.setFechaSalida(
                dto.getFechaSalida()
        );

        vuelo.setHoraSalida(
                dto.getHoraSalida()
        );

        vuelo.setFechaLlegada(
                dto.getFechaLlegada()
        );

        vuelo.setHoraLlegada(
                dto.getHoraLlegada()
        );

        vuelo.setEstado(
                "PROGRAMADO"
        );

        vuelo.setPrecioClaseEconomica(
                dto.getPrecioEconomica()
        );

        vuelo.setPrecioClaseEjecutiva(
                dto.getPrecioEjecutiva()
        );

        // ASIENTOS DISPONIBLES
        int totalAsientos =
                avion.getCantAsientosEconomica()
                        +
                        avion.getCantAsientosEjecutiva();

        vuelo.setAsientosDisponibles(
                totalAsientos
        );

        PaqTripulacion paquete =
                tripulacionPaqueteRepository.findById(dto.getTripulacionId())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Paquete de tripulación no encontrado"
                                )
                        );

        vuelo.setTripulacion(paquete);

        // AUDITORÍA
        vuelo.setFechaHoraCreacion(
                LocalDateTime.now()
        );

        vuelo.setUsuarioCreacion(
                dto.getUsuario()
        );

        return vueloRepository.save(vuelo);
    }

    // OBTENER AVIONES ACTIVOS
    public List<Avion> obtenerAvionesActivos(
            Integer aerolineaId
    ) {

        return avionRepository
                .findByAerolineaAerolineaIdAndEstadoIgnoreCase(
                        aerolineaId,
                        "Activo"
                );
    }

    // BUSCAR VUELOS DISPONIBLES
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
                            "$" + vuelo.getPrecioClaseEconomica()
                    )

                    .ejecutiva(
                            "$" + vuelo.getPrecioClaseEjecutiva()
                    )

                    .build();

        }).collect(Collectors.toList());
    }

    // MATRIZ DE ASIENTOS
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
                                )
                        );

        Integer avionId =
                vuelo.getAvion()
                        .getAvionId();

        return avionService
                .obtenerEstructuraAsientos(
                        avionId
                );
    }

    // CONSULTA DE VUELOS
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


    public List<Vuelo> obtenerPendientesAbordaje() {

        List<Vuelo> vuelos =
                vueloRepository.findByEstado("PENDIENTE ABORDAR");

        return vuelos;
    }



    @Transactional
    public void actualizarEstadoVuelo(Long id, String nuevoEstado) {
        // 1. Buscar el vuelo por su ID
        Optional<Vuelo> vueloOpt = vueloRepository.findById(id);

        if (vueloOpt.isPresent()) {
            Vuelo vuelo = vueloOpt.get();
            vuelo.setEstado(nuevoEstado);
            vueloRepository.save(vuelo);
        } else {
            // 3. Si no existe, lanzamos un error que el Controller atrapará
            throw new RuntimeException("Vuelo no encontrado con ID: " + id);
        }
    }




    public boolean existeChoqueHorarios(ValidacionChoqueHorarioDTO dto) {
        if (dto.getVuelosExistentesIds() == null || dto.getVuelosExistentesIds().isEmpty()) {
            return false;
        }

        Vuelo vueloNuevo = vueloRepository.findById(dto.getVueloNuevoId())
                .orElseThrow(() -> new RuntimeException("Vuelo nuevo no encontrado con ID: " + dto.getVueloNuevoId()));

        LocalDateTime inicioNuevo = LocalDateTime.of(vueloNuevo.getFechaSalida(), vueloNuevo.getHoraSalida());
        LocalDateTime finNuevo = LocalDateTime.of(vueloNuevo.getFechaLlegada(), vueloNuevo.getHoraLlegada());


        List<Vuelo> vuelosExistentes = vueloRepository.findAllById(dto.getVuelosExistentesIds());

        //comparar
        for (Vuelo vExistente : vuelosExistentes) {
            LocalDateTime inicioExistente = LocalDateTime.of(vExistente.getFechaSalida(), vExistente.getHoraSalida());
            LocalDateTime finExistente = LocalDateTime.of(vExistente.getFechaLlegada(), vExistente.getHoraLlegada());

            // inicio nuevo < fin Existente Y inicio existente < fin nuevo)
            if (inicioNuevo.isBefore(finExistente) && inicioExistente.isBefore(finNuevo)) {
                return true;
            }
        }

        return false;
    }


    public List<Avion> obtenerAvionesDisponibles(

            Integer aerolineaId,

            LocalDate fechaSalida,

            LocalTime horaSalida,

            LocalDate fechaLlegada,

            LocalTime horaLlegada
    ) {

        List<Avion> aviones =
                avionRepository
                        .findByAerolineaAerolineaIdAndEstadoIgnoreCase(
                                aerolineaId,
                                "Activo"
                        );

        LocalDateTime salida =
                LocalDateTime.of(
                        fechaSalida,
                        horaSalida
                );

        LocalDateTime llegada =
                LocalDateTime.of(
                        fechaLlegada,
                        horaLlegada
                );

        return aviones.stream()

                .filter(avion ->

                        !vueloRepository
                                .existeConflictoHorario(
                                        avion.getAvionId(),
                                        salida,
                                        llegada
                                )
                )

                .toList();
    }
    // CONSULTAR VUELO POR ID
    public ConsultaVueloDTO consultarVueloPorId(
            Long vueloId
    ) {

        Vuelo vuelo =
                vueloRepository.findById(vueloId)
                        .orElse(null);

        if (vuelo == null) {

            return null;
        }

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

        return dto;
    }

    public List<ConsultaPasajerosVueloDTO> consultarPasajerosPorVuelo(Long vueloId) {

        List<Object[]> resultados =
                boletoRepository.consultarPasajerosPorVuelo(vueloId);

        System.out.println("RESULTADOS:");
        System.out.println(resultados);

        List<ConsultaPasajerosVueloDTO> pasajeros =
                new ArrayList<>();

        for (Object[] fila : resultados) {

            System.out.println(Arrays.toString(fila));

            ConsultaPasajerosVueloDTO dto =
                    new ConsultaPasajerosVueloDTO();

            dto.setNombrePasajero(
                    fila[0] != null
                            ? fila[0].toString()
                            : ""
            );

            dto.setNumeroPasaporte(
                    fila[1] != null
                            ? fila[1].toString()
                            : ""
            );

            dto.setNacionalidad(
                    fila[2] != null
                            ? fila[2].toString()
                            : ""
            );

            dto.setEdad(
                    fila[3] != null
                            ? ((Number) fila[3]).intValue()
                            : 0
            );

            dto.setTelefono(
                    fila[4] != null
                            ? fila[4].toString()
                            : ""
            );

            dto.setCorreo(
                    fila[5] != null
                            ? fila[5].toString()
                            : ""
            );

            pasajeros.add(dto);
        }

        return pasajeros;
    }

}