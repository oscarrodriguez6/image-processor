package com.imageProcessor.dto;

public class ClusterDTO {
    private double latitud;
    private double longitud;
    private int cantidad;
    private double radio;

    public ClusterDTO(double latitud, double longitud, int cantidad, double radio) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.cantidad = cantidad;
        this.radio = radio;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }
}
