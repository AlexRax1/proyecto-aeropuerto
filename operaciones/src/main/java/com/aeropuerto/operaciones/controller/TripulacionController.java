package com.aeropuerto.operaciones.controller;

import com.aeropuerto.operaciones.dto.CrearPaqTripulacionDTO;
import com.aeropuerto.operaciones.model.PaqTripulacion;
import com.aeropuerto.operaciones.service.TripulacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tripulacion")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TripulacionController {

    private final TripulacionService service;

    @PostMapping("/paquete")
    public ResponseEntity<PaqTripulacion>
    crearPaquete(

            @RequestBody
            CrearPaqTripulacionDTO dto
    ) {

        return ResponseEntity.ok(
                service.crearPaquete(dto)
        );
    }

}
