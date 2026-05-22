package com.aeropuerto.operaciones.service;

import com.aeropuerto.operaciones.model.PersonalTripulacion;
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
    private PersonalTripulacionRepository personalTripulacionRepository;

    public List<PersonalTripulacion> obtenerTripulacionDisponible(

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

        return personalTripulacionRepository.findAll()
                .stream()

                .filter(t -> !personalTripulacionRepository
                        .existeConflictoHorario(

                                t.getPersonalTripulacionId(),

                                salida,

                                llegada
                        )
                )

                .toList();
    }

    public List<PersonalTripulacion>
    obtenerPorRol(String rol) {

        return personalTripulacionRepository
                .findByRolNombreRolTripulacionAndEstadoIgnoreCase(
                        rol,
                        "ACTIVO"
                );
    }
}