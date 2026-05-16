package com.aeropuerto.operaciones.controller;


import com.aeropuerto.operaciones.dto.ConsultaVueloDTO;
import com.aeropuerto.operaciones.dto.EstructuraAvionDTO;
import com.aeropuerto.operaciones.dto.VueloDisponibleDTO;
import com.aeropuerto.operaciones.service.VueloService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vuelos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class VueloController {

    private final VueloService vueloService;

    @GetMapping("/buscar")
    public ResponseEntity<List<VueloDisponibleDTO>> buscarVuelos(
            @RequestParam Long origenId,
            @RequestParam Long destinoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida) {

        List<VueloDisponibleDTO> vuelos = vueloService.buscarVuelos(origenId, destinoId, fechaSalida);
        return ResponseEntity.ok(vuelos);
    }

    // Endpoint para obtener la matriz de los asientos
    @GetMapping("/{id}/asientos")
    public ResponseEntity<EstructuraAvionDTO> obtenerAsientosDeVuelo(@PathVariable Long id) {
        EstructuraAvionDTO estructura = vueloService.obtenerMatrizPorVuelo(id);
        return ResponseEntity.ok(estructura);
    }

    @GetMapping("/consulta")
    public List<ConsultaVueloDTO> consultarVuelos(

            @RequestParam LocalDate fechaDesde,
            @RequestParam LocalDate fechaHasta
    ) {

        return vueloService.consultarVuelos(
                fechaDesde,
                fechaHasta
        );
    }
}
