package com.aeropuerto.reservas.controller;

import com.aeropuerto.reservas.dto.RequestAbordajeDTO;
import com.aeropuerto.reservas.model.Boleto;
import com.aeropuerto.reservas.model.Equipaje;
import com.aeropuerto.reservas.repository.BoletoRepository;
import com.aeropuerto.reservas.repository.EquipajeRepository;
import com.aeropuerto.reservas.service.BoletoService;
import com.aeropuerto.reservas.service.ReservaRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final BoletoService boletoService;

    private final ReservaRedisService redisService;


    @Autowired
    private  EquipajeRepository equipajeRepository;


    @Autowired
    private BoletoRepository boletoRepository;

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



    @PutMapping("/abordar")
    @Transactional
    public ResponseEntity<String> registrarAbordaje(@RequestBody RequestAbordajeDTO request) {

        Optional<Boleto> boletoOpt =
                boletoRepository.findByVueloIdAndUsuarioIdAndEstado(
                        request.getIdVuelo(),
                        request.getIdusuario(),
                        "PAGADO"
                );

        if (boletoOpt.isEmpty()) {

            return ResponseEntity.status(404)
                    .body("Error: Boleto no encontrado o pasajero no registrado");
        }

        Boleto boleto = boletoOpt.get();

        if (!"PENDIENTE".equals(boleto.getEstadoAbordaje())) {

            return ResponseEntity.status(400)
                    .body("Error: El pasajero ya registró su abordaje previamente (Estado actual: "
                            + boleto.getEstadoAbordaje() + ")");
        }

        boleto.setEstadoAbordaje("ABORDADO");

        // cálculo de maletas extra
        int maletasExtras =
                request.getNumMaletas() - boleto.getCantMaletas();

        if (maletasExtras > 0) {

            boleto.setCantMaletasExtra(maletasExtras);

            BigDecimal cobroExtra =
                    BigDecimal.valueOf(maletasExtras * 50.0);

            boleto.setMontoExtra(cobroExtra);
        }

        boletoRepository.save(boleto);

        // guardar equipajes
        if (request.getMaletas() != null &&
                !request.getMaletas().isEmpty()) {

            for (Equipaje equipaje : request.getMaletas()) {

                equipaje.setBoletoId(boleto.getBoletoId());

                equipaje.setNombreUsuario(
                        request.getNombrePasajero()
                );

                equipajeRepository.save(equipaje);
            }
        }

        return ResponseEntity.ok("Abordaje registrado con éxito");
    }

    @PutMapping("/vuelo/{vueloId}/finalizar")
    public ResponseEntity<String> finalizarAbordaje(@PathVariable Integer vueloId) {

        int boletosCancelados = boletoRepository.cancelarBoletosPendientes(vueloId);

        return ResponseEntity.ok( boletosCancelados + " boletos que no se presentaron.");
    }
}