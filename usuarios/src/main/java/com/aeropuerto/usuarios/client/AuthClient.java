package com.aeropuerto.usuarios.client;

import com.aeropuerto.usuarios.dto.AuthRegisterRequest;
import com.aeropuerto.usuarios.dto.AuthRegisterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-client", url = "http://localhost:8081/auth")
public interface AuthClient {

    @PostMapping("/register")
    ResponseEntity<AuthRegisterResponse> registrarCredenciales(@RequestBody AuthRegisterRequest request);

    @DeleteMapping("/delete/{userId}")
    ResponseEntity<Void> eliminarCredenciales(@PathVariable("userId") Integer userId);
}