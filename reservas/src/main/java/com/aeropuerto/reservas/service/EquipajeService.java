package com.aeropuerto.reservas.service;

import com.aeropuerto.reservas.dto.EquipajeVueloDTO;
import com.aeropuerto.reservas.repository.EquipajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipajeService {

    private final EquipajeRepository equipajeRepository;

    // Inyección de dependencias mediante el constructor (la mejor práctica en Spring)
    public EquipajeService(EquipajeRepository equipajeRepository) {
        this.equipajeRepository = equipajeRepository;
    }

    @Transactional(readOnly = true)
    public List<EquipajeVueloDTO> obtenerPorVuelo(Integer vueloId) {

        // Aquí llamas a la consulta JPQL que definimos en el repositorio
        return equipajeRepository.findEquipajesByVueloId(vueloId);

    }
}