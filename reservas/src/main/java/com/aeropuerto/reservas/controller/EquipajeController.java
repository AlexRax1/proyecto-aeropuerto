package com.aeropuerto.reservas.controller;

import com.aeropuerto.reservas.dto.EquipajeVueloDTO;
import com.aeropuerto.reservas.service.EquipajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipajes")
@CrossOrigin("*")
public class EquipajeController {

    private final EquipajeService equipajeService;

    public EquipajeController(EquipajeService equipajeService) {
        this.equipajeService = equipajeService;
    }

    @GetMapping("/vuelo/{vueloId}")
    public ResponseEntity<List<EquipajeVueloDTO>> obtenerEquipajePorVuelo(@PathVariable Integer vueloId) {
        List<EquipajeVueloDTO> equipajes = equipajeService.obtenerPorVuelo(vueloId);
        return ResponseEntity.ok(equipajes);
    }
}