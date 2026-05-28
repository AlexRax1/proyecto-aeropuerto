package com.aeropuerto.operaciones.controller;


import com.aeropuerto.operaciones.dto.CrearPaqTripulacionDTO;
import com.aeropuerto.operaciones.model.PaqTripulacion;
import com.aeropuerto.operaciones.model.PersonalTripulacion;
import com.aeropuerto.operaciones.service.PersonalTripulacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/tripulacion")
@RequiredArgsConstructor
public class PersonalTripulacionController {

    private final PersonalTripulacionService personalTripulacionService;

    @GetMapping("/disponible")
    public List<PersonalTripulacion> obtenerTripulacionDisponible(

            @RequestParam LocalDate fechaSalida,

            @RequestParam LocalTime horaSalida,

            @RequestParam LocalDate fechaLlegada,

            @RequestParam LocalTime horaLlegada

    ) {

        return personalTripulacionService.obtenerTripulacionDisponible(

                fechaSalida,
                horaSalida,
                fechaLlegada,
                horaLlegada
        );
    }

    @GetMapping("/rol")
    public List<PersonalTripulacion>
    obtenerPorRol(

            @RequestParam String rol

    ) {

        return personalTripulacionService
                .obtenerPorRol(rol);
    }

    @PostMapping("/crear-paquete")
    public ResponseEntity<PaqTripulacion>
    crearPaquete(

            @RequestBody
            CrearPaqTripulacionDTO dto
    ) {

        return ResponseEntity.ok(
                personalTripulacionService
                        .crearPaquete(dto)
        );
    }

    @GetMapping("/paquetes")
    public List<PaqTripulacion>
    obtenerPaquetes() {

        return personalTripulacionService
                .obtenerPaquetes();
    }
}

