package com.aeropuerto.operaciones.service;

import com.aeropuerto.operaciones.dto.CrearPaqTripulacionDTO;
import com.aeropuerto.operaciones.model.PaqTripulacion;
import com.aeropuerto.operaciones.repository.PaqTripulacionRepository;
import com.aeropuerto.operaciones.repository.PersonalTripulacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripulacionService {

    private final PaqTripulacionRepository paqRepo;

    private final PersonalTripulacionRepository personalRepo;

    public PaqTripulacion crearPaquete12(
            CrearPaqTripulacionDTO dto
    ) {

        PaqTripulacion paquete =
                new PaqTripulacion();

        paquete.setNombrePaquete(
                dto.getNombrePaquete()
        );

        paquete.setPiloto(
                personalRepo.findById(
                        Long.valueOf(dto.getPilotoId())
                ).orElseThrow()
        );

        paquete.setCopiloto(
                personalRepo.findById(
                        Long.valueOf(dto.getCopilotoId())
                ).orElseThrow()
        );

        paquete.setIngeniero(
                personalRepo.findById(
                        Long.valueOf(dto.getIngenieroId())
                ).orElseThrow()
        );

        paquete.setSobrecargo1(
                personalRepo.findById(
                        Long.valueOf(dto.getSobrecargo1Id())
                ).orElseThrow()
        );

        paquete.setSobrecargo2(
                personalRepo.findById(
                        Long.valueOf(dto.getSobrecargo2Id())
                ).orElseThrow()
        );

        paquete.setSobrecargo3(
                personalRepo.findById(
                        Long.valueOf(dto.getSobrecargo3Id())
                ).orElseThrow()
        );

        return paqRepo.save(paquete);
    }

    public List<PaqTripulacion> obtenerPaquetes() {

        return paqRepo.findAll();
    }


}
