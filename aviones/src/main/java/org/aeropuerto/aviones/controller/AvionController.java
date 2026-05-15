package org.aeropuerto.aviones.controller;

import org.aeropuerto.aviones.model.Avion;
import org.aeropuerto.aviones.repository.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aviones")
@CrossOrigin("*")
public class AvionController {

    @Autowired
    private AvionRepository avionRepository;

    @GetMapping("/aerolinea/{id}")
    public List<Avion> obtenerPorAerolinea(
            @PathVariable Integer id
    ) {

        return avionRepository.findByAerolineaId(id);

    }

}
