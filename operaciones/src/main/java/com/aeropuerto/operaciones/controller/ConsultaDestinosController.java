package com.aeropuerto.operaciones.controller;

import com.aeropuerto.operaciones.dto.DestinoAerolineaDTO;
import com.aeropuerto.operaciones.repository.DestinoAsignacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/consulta-destinos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ConsultaDestinosController {

    private final DestinoAsignacionRepository repository;

    @GetMapping("/aerolineas")
    public List<String> obtenerAerolineas() {

        return repository.obtenerAerolineas();
    }

    @GetMapping("/{aerolinea}")
    public List<DestinoAerolineaDTO> consultar(
            @PathVariable String aerolinea
    ) {

        List<Object[]> resultados =
                repository.buscarDestinosPorAerolinea(aerolinea);

        List<DestinoAerolineaDTO> lista = new ArrayList<>();

        for (Object[] fila : resultados) {

            DestinoAerolineaDTO dto =
                    new DestinoAerolineaDTO();

            dto.setNombreAeropuerto((String) fila[0]);
            dto.setPaisDestino((String) fila[1]);
            dto.setCiudadDestino((String) fila[2]);

            lista.add(dto);
        }

        return lista;
    }
}
