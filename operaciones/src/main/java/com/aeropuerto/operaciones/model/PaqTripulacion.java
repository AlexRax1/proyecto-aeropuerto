package com.aeropuerto.operaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "paq_tripulacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaqTripulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "paq_tripulacion_id")
    private Long paqTripulacionId;

    @Column(name = "tripulacion_id")
    private Integer tripulacionId;

    @Column(name = "nombre_paquete")
    private String nombrePaquete;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_hora_creacion")
    private LocalDateTime fechaHoraCreacion;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    // =====================================
    // AEROLÍNEA
    // =====================================

    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;

    // =====================================
    // PILOTO
    // =====================================

    @ManyToOne
    @JoinColumn(name = "piloto_id")
    private PersonalTripulacion piloto;

    @ManyToOne
    @JoinColumn(name = "copiloto_id")
    private PersonalTripulacion copiloto;

    @ManyToOne
    @JoinColumn(name = "ingeniero_id")
    private PersonalTripulacion ingeniero;

    @ManyToOne
    @JoinColumn(name = "sobrecargo1_id")
    private PersonalTripulacion sobrecargo1;

    @ManyToOne
    @JoinColumn(name = "sobrecargo2_id")
    private PersonalTripulacion sobrecargo2;

    @ManyToOne
    @JoinColumn(name = "sobrecargo3_id")
    private PersonalTripulacion sobrecargo3;
}