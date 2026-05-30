package com.aeropuerto.operaciones.controller;

import com.aeropuerto.operaciones.dto.ConsultaAerolineaDTO;
import com.aeropuerto.operaciones.repository.DestinoAsignacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulta-aerolineas")
@RequiredArgsConstructor
public class ConsultaAerolineaController {

    private final DestinoAsignacionRepository repository;

    @GetMapping("/{aeropuerto}")
    public List<ConsultaAerolineaDTO> consultar(
            @PathVariable String aeropuerto
    ) {

        return repository.consultarAerolineas(aeropuerto);
    }
}
