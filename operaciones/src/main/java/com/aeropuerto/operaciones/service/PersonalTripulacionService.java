package com.aeropuerto.operaciones.service;

import com.aeropuerto.operaciones.dto.CrearPaqTripulacionDTO;
import com.aeropuerto.operaciones.model.PaqTripulacion;
import com.aeropuerto.operaciones.model.PersonalTripulacion;
import com.aeropuerto.operaciones.repository.PaqTripulacionRepository;
import com.aeropuerto.operaciones.repository.PersonalTripulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PersonalTripulacionService {

    @Autowired
    private PersonalTripulacionRepository
            personalTripulacionRepository;

    @Autowired
    private PaqTripulacionRepository
            paqTripulacionRepository;

    // =====================================
    // DISPONIBILIDAD
    // =====================================

    public List<PersonalTripulacion>
    obtenerTripulacionDisponible(

            LocalDate fechaSalida,

            LocalTime horaSalida,

            LocalDate fechaLlegada,

            LocalTime horaLlegada
    ) {

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

        return personalTripulacionRepository
                .findAll()
                .stream()

                .filter(t ->

                        !personalTripulacionRepository
                                .existeConflictoHorario(

                                        t.getPersonalTripulacionId(),

                                        salida,

                                        llegada
                                )
                )

                .toList();
    }

    // =====================================
    // CONSULTAR POR ROL
    // =====================================

    public List<PersonalTripulacion>
    obtenerPorRol(String rol) {

        return personalTripulacionRepository
                .findByRolNombreRolTripulacionAndEstadoIgnoreCase(

                        rol,

                        "ACTIVO"
                );
    }

    // =====================================
    // CREAR PAQUETE
    // =====================================
    public PaqTripulacion crearPaquete(
            CrearPaqTripulacionDTO dto
    ) {

        PaqTripulacion paquete =
                new PaqTripulacion();

        paquete.setNombrePaquete(
                dto.getNombrePaquete()
        );

        paquete.setPiloto(
                actualizarEstadoTripulacion(
                        Integer.valueOf(dto.getPilotoId()),
                        "EN VUELO"
                )
        );

        paquete.setCopiloto(
                actualizarEstadoTripulacion(
                        Integer.valueOf(dto.getCopilotoId()),
                        "EN VUELO"
                )
        );

        paquete.setIngeniero(
                actualizarEstadoTripulacion(
                        Integer.valueOf(dto.getIngenieroId()),
                        "EN VUELO"
                )
        );

        paquete.setSobrecargo1(
                actualizarEstadoTripulacion(
                        Integer.valueOf(dto.getSobrecargo1Id()),
                        "EN VUELO"
                )
        );

        paquete.setSobrecargo2(
                actualizarEstadoTripulacion(
                        Integer.valueOf(dto.getSobrecargo2Id()),
                        "EN VUELO"
                )
        );

        paquete.setSobrecargo3(
                actualizarEstadoTripulacion(
                        Integer.valueOf(dto.getSobrecargo3Id()),
                        "EN VUELO"
                )
        );

        paquete.setEstado("DISPONIBLE");

        return paqTripulacionRepository.save(paquete);
    }

    // =====================================
    // LISTAR PAQUETES
    // =====================================

    public List<PaqTripulacion> obtenerPaquetes() {

        return paqTripulacionRepository
                .findByEstado("DISPONIBLE");
    }

    private PersonalTripulacion actualizarEstadoTripulacion(
            Integer id,
            String estado
    ) {

        PersonalTripulacion persona =
                personalTripulacionRepository
                        .findById(Long.valueOf(id))
                        .orElseThrow();

        persona.setEstado(estado);

        return personalTripulacionRepository
                .save(persona);
    }
}