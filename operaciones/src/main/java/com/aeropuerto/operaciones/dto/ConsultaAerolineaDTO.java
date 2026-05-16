package com.aeropuerto.operaciones.dto;

public class ConsultaAerolineaDTO {

    private String nombreAerolinea;
    private Integer cantidadAviones;
    private String ciudadDestino;
    private String paisDestino;

    public ConsultaAerolineaDTO(
            String nombreAerolinea,
            Integer cantidadAviones,
            String ciudadDestino,
            String paisDestino
    ) {

        this.nombreAerolinea = nombreAerolinea;
        this.cantidadAviones = cantidadAviones;
        this.ciudadDestino = ciudadDestino;
        this.paisDestino = paisDestino;
    }

    // GETTERS Y SETTERS

    public String getNombreAerolinea() {
        return nombreAerolinea;
    }

    public void setNombreAerolinea(String nombreAerolinea) {
        this.nombreAerolinea = nombreAerolinea;
    }

    public Integer getCantidadAviones() {
        return cantidadAviones;
    }

    public void setCantidadAviones(Integer cantidadAviones) {
        this.cantidadAviones = cantidadAviones;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public String getPaisDestino() {
        return paisDestino;
    }

    public void setPaisDestino(String paisDestino) {
        this.paisDestino = paisDestino;
    }

}
