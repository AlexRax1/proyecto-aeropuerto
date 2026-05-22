package com.aeropuerto.reservas.service;

import com.aeropuerto.reservas.client.OperacionesClient;
import com.aeropuerto.reservas.dto.ValidacionChoqueHorarioDTO;
import com.aeropuerto.reservas.model.Boleto;
import com.aeropuerto.reservas.repository.BoletoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BoletoService {

    private final BoletoRepository boletoRepository;

    private final OperacionesClient operacionesClient;

    public BoletoService(BoletoRepository boletoRepository, OperacionesClient operacionesClient) {
        this.boletoRepository = boletoRepository;
        this.operacionesClient = operacionesClient;
    }

    public List<Long> obtenerAsientosOcupados(Long vueloId) {
        return boletoRepository.findAsientosOcupadosByVueloId(vueloId);
    }

    public Boleto crearReserva(Boleto boleto) {
        List<Long> vuelosDelUsuario = boletoRepository.findVuelosActivosPorUsuario(boleto.getUsuarioId());
        if (vuelosDelUsuario != null && !vuelosDelUsuario.isEmpty()) {

            ValidacionChoqueHorarioDTO dto = new ValidacionChoqueHorarioDTO();
            dto.setVueloNuevoId(boleto.getVueloId());
            dto.setVuelosExistentesIds(vuelosDelUsuario);

            boolean hayChoque = operacionesClient.validarChoqueHorarios(dto);

            if (hayChoque) {
                throw new RuntimeException("El horario de este vuelo choca con otro vuelo que ya tienes reservado.");
            }
        }


        boleto.setEstado("PAGADO");
        return boletoRepository.save(boleto);
    }
}