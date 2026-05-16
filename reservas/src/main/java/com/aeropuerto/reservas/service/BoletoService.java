package com.aeropuerto.reservas.service;

import com.aeropuerto.reservas.model.Boleto;
import com.aeropuerto.reservas.repository.BoletoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BoletoService {

    private final BoletoRepository boletoRepository;

    public BoletoService(BoletoRepository boletoRepository) {
        this.boletoRepository = boletoRepository;
    }

    public List<Long> obtenerAsientosOcupados(Long vueloId) {
        return boletoRepository.findAsientosOcupadosByVueloId(vueloId);
    }

    public Boleto crearReserva(Boleto boleto) {
        boleto.setEstado("PAGADO");
        return boletoRepository.save(boleto);
    }
}