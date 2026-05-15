package com.aeropuerto.operaciones.controller;


import com.aeropuerto.operaciones.dto.DestinoSelectDTO;
import com.aeropuerto.operaciones.service.DestinoAeropuertoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/operaciones/destinos")
@CrossOrigin("*") // Permite peticiones desde Angular durante el desarrollo
@RequiredArgsConstructor
public class DestinoAeropuertoController {

    private final DestinoAeropuertoService destinoService;

    @GetMapping("/select")
    public ResponseEntity<List<DestinoSelectDTO>> listarDestinos() {
        return ResponseEntity.ok(destinoService.obtenerDestinosParaSelect());
    }
}
// algo simple