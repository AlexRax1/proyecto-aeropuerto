package com.aeropuerto.operaciones.controller;

import com.aeropuerto.operaciones.dto.CrearPaqTripulacionDTO;
import com.aeropuerto.operaciones.model.PaqTripulacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tripulacion")
@RequiredArgsConstructor
public class TripulacionController {

    private final PersonalTripulacionController service;

    @PostMapping("/paquete")
    public ResponseEntity<PaqTripulacion> crearPaquete(@RequestBody CrearPaqTripulacionDTO dto) {
        PaqTripulacion paquete = service.crearPaquete(dto).getBody();
        return ResponseEntity.ok()
                // Inyectamos el ID del nuevo paquete
                .header("X-Afectado-Id", String.valueOf(paquete.getPaqTripulacionId()))
                .body(paquete);
    }

}
