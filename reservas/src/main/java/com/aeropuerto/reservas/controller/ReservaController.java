package com.aeropuerto.reservas.controller;

import com.aeropuerto.reservas.model.Boleto;
import com.aeropuerto.reservas.service.BoletoService;
import com.aeropuerto.reservas.service.ReservaRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final BoletoService boletoService;

    private final ReservaRedisService redisService;

    public ReservaController(BoletoService boletoService, ReservaRedisService redisService) {
        this.boletoService = boletoService;
        this.redisService = redisService;
    }

    @GetMapping("/vuelo/{vueloId}/ocupados")
    public ResponseEntity<Map<String, List<Long>>> obtenerOcupados(@PathVariable Long vueloId) {
        Map<String, List<Long>> estadoCompleto = new HashMap<>();
        estadoCompleto.put("ocupados", boletoService.obtenerAsientosOcupados(vueloId));

        // para lo de redis
        estadoCompleto.put("bloqueados", redisService.obtenerAsientosBloqueados(vueloId));

        return ResponseEntity.ok(estadoCompleto);
    }

    @PostMapping("/iniciar-pago")
    public ResponseEntity<?> iniciarPago( @RequestHeader("X-User-Id") Long usuarioIdAutenticado, @RequestBody Boleto boleto) {

        boleto.setUsuarioId(usuarioIdAutenticado);

        boolean bloqueado = redisService.bloquearAsiento(
                boleto.getVueloId(),
                boleto.getAsientoId(),
                boleto.getUsuarioId()
        );

        if (!bloqueado) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El asiento ya está ocupado o en proceso de pago por otro usuario.");
        }

        return ResponseEntity.ok("Bloqueo exitoso. Tienes 5 minutos para completar el pago.");
    }


    //para confirmar la reserva DESPUES del pago
    @PostMapping("/crear")
    public ResponseEntity<Boleto> registrarBoleto(@RequestHeader("X-User-Id") Long usuarioIdAutenticado, @RequestBody Boleto boleto) {
        // hacer validaciones
        boleto.setUsuarioId(usuarioIdAutenticado);
        
        Boleto nuevaReserva = boletoService.crearReserva(boleto);
        //quitar el bloqueo de redis
        redisService.liberarAsiento(boleto.getVueloId(), boleto.getAsientoId());
        return ResponseEntity.ok(nuevaReserva);
    }
}