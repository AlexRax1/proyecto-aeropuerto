package com.aeropuerto.operaciones.controller;


import com.aeropuerto.operaciones.dto.AvionRequestDTO;
import com.aeropuerto.operaciones.model.Avion;
import com.aeropuerto.operaciones.service.AvionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aviones")
@RequiredArgsConstructor
public class AvionController {

    private final AvionService avionService;



    //crear aviones y sus asientos automaticamnte
    @PostMapping
    public ResponseEntity<Avion> crearAvion(@RequestBody AvionRequestDTO requestDTO) {
        Avion nuevoAvion = avionService.crearAvionConAsientos(requestDTO);
        return new ResponseEntity<>(nuevoAvion, HttpStatus.CREATED);
    }




}
