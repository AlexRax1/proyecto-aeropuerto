package com.aeropuerto.operaciones.service;

import com.aeropuerto.operaciones.dto.DestinoSelectDTO;
import com.aeropuerto.operaciones.repository.DestinoAeropuertoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinoAeropuertoService {

    private final DestinoAeropuertoRepository destinoRepository;

    public List<DestinoSelectDTO> obtenerDestinosParaSelect() {
        return destinoRepository.findAll().stream()
                .map(destino -> new DestinoSelectDTO(
                        destino.getDestinoId(),
                        String.format("%s, %s, %s",
                                destino.getNombreAeropuerto(),
                                destino.getCiudadDestino(),
                                destino.getPaisDestino())
                ))
                .collect(Collectors.toList());
    }

}
