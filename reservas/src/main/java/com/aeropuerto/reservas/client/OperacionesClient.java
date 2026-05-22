package com.aeropuerto.reservas.client;


import com.aeropuerto.reservas.dto.ValidacionChoqueHorarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "operaciones-client", url = "http://localhost:8083/")
public interface OperacionesClient {

    @PostMapping("/vuelos/validar-choque")
    boolean validarChoqueHorarios(@RequestBody ValidacionChoqueHorarioDTO peticion);
}