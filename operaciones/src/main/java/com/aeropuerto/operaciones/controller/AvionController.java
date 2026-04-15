package com.aeropuerto.operaciones.controller;


import com.aeropuerto.operaciones.dto.AvionRequestDTO;
import com.aeropuerto.operaciones.dto.EstructuraAvionDTO;
import com.aeropuerto.operaciones.model.Avion;
import com.aeropuerto.operaciones.service.AvionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aviones")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AvionController {

    private final AvionService avionService;



    //crear aviones y sus asientos automaticamnte
    @PostMapping
    public ResponseEntity<Avion> crearAvion(@RequestBody AvionRequestDTO requestDTO) {
        Avion nuevoAvion = avionService.crearAvionConAsientos(requestDTO);
        return new ResponseEntity<>(nuevoAvion, HttpStatus.CREATED);
    }



    // Endpoint para obtener la matriz de los asientos
    @GetMapping("/{id}/asientos")
    public ResponseEntity<EstructuraAvionDTO> obtenerEstructuraAsientos(@PathVariable Long id) {
        EstructuraAvionDTO estructura = avionService.obtenerEstructuraAsientos(id);
        return ResponseEntity.ok(estructura);
    }
}
