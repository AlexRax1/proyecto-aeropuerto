package com.aeropuerto.operaciones.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "boleto")
public class Boleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boleto_id")
    private Integer boletoId;

    @Column(name = "vuelo_id")
    private Integer vueloId;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "asiento_id")
    private Integer asientoId;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cant_maletas")
    private Integer cantMaletas;
}
