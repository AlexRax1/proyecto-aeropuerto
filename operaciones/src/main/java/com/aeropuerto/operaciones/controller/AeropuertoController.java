package com.aeropuerto.operaciones.controller;

import com.aeropuerto.operaciones.model.DestinoAeropuerto;
import com.aeropuerto.operaciones.repository.DestinoAeropuertoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aeropuertos")
@RequiredArgsConstructor
public class AeropuertoController {

    private final DestinoAeropuertoRepository repository;

    @GetMapping
    public List<String> obtenerAeropuertos() {

        return repository.findAll()
                .stream()
                .map(DestinoAeropuerto::getNombreAeropuerto)
                .distinct()
                .collect(Collectors.toList());
    }
}
