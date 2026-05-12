package com.aeropuerto.reservas.controller;

import com.aeropuerto.reservas.model.Boleto;
import com.aeropuerto.reservas.service.BoletoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    private final BoletoService boletoService;

    public ReservaController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @GetMapping("/vuelo/{vueloId}/ocupados")
    public ResponseEntity<List<Long>> obtenerOcupados(@PathVariable Long vueloId) {
        return ResponseEntity.ok(boletoService.obtenerAsientosOcupados(vueloId));
    }

    @PostMapping("/crear")
    public ResponseEntity<Boleto> registrarBoleto(@RequestBody Boleto boleto) {
        // hacer validaciones
        Boleto nuevaReserva = boletoService.crearReserva(boleto);
        return ResponseEntity.ok(nuevaReserva);
    }
}