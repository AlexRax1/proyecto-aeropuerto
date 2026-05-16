package com.aeropuerto.operaciones.controller;

import com.aeropuerto.operaciones.dto.AvionRequestDTO;
import com.aeropuerto.operaciones.model.Avion;
import com.aeropuerto.operaciones.repository.AvionRepository;
import com.aeropuerto.operaciones.service.AvionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/aviones")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AvionController {

    private final AvionService avionService;

    @Autowired
    private AvionRepository avionRepository;

    // ==========================
    // CONSULTAR AVIONES
    // ==========================

    @GetMapping("/aerolinea/{id}")
    public List<Avion> obtenerPorAerolinea(
            @PathVariable Integer id
    ) {

        return avionRepository.findByAerolinea_AerolineaId(id);
    }

    // ==========================
    // CREAR AVIÓN
    // ==========================

    @PostMapping
    public ResponseEntity<Avion> crearAvion(
            @RequestBody AvionRequestDTO requestDTO
    ) {

        Avion nuevoAvion =
                avionService.crearAvionConAsientos(requestDTO);

        return new ResponseEntity<>(
                nuevoAvion,
                HttpStatus.CREATED
        );
    }
}
