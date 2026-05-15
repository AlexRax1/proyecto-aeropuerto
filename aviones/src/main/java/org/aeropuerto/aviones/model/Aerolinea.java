package org.aeropuerto.aviones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aerolineas")
public class Aerolinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aerolinea_id")
    private Integer aerolineaId;

    @Column(name = "nombre_aerolinea")
    private String nombreAerolinea;

    @Column(name = "cant_aviones")
    private Integer cantAviones;

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Integer getAerolineaId() {
        return aerolineaId;
    }

    public void setAerolineaId(Integer aerolineaId) {
        this.aerolineaId = aerolineaId;
    }

    public String getNombreAerolinea() {
        return nombreAerolinea;
    }

    public void setNombreAerolinea(String nombreAerolinea) {
        this.nombreAerolinea = nombreAerolinea;
    }

    public Integer getCantAviones() {
        return cantAviones;
    }

    public void setCantAviones(Integer cantAviones) {
        this.cantAviones = cantAviones;
    }
}
