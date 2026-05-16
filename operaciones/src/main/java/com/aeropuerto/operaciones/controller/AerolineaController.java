package com.aeropuerto.operaciones.controller;

import com.aeropuerto.operaciones.model.Aerolinea;
import com.aeropuerto.operaciones.repository.AerolineaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aerolineas")
@CrossOrigin(origins = "http://localhost:4200")
public class AerolineaController {

    private final AerolineaRepository aerolineaRepository;

    public AerolineaController(
            AerolineaRepository aerolineaRepository
    ) {

        this.aerolineaRepository = aerolineaRepository;
    }

    @GetMapping
    public List<Aerolinea> obtenerAerolineas() {

        return aerolineaRepository.findAll();
    }
}
