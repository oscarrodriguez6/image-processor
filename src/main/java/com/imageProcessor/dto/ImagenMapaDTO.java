package com.imageProcessor.dto;

public class ImagenMapaDTO {

    public Long id;
    public Double longitud;
    public Double latitud;
    public String ruta;
    public String urlMiniatura;

    public ImagenMapaDTO(Long id, Double longitud, Double latitud, String ruta, String urlMiniatura) {
        this.id = id;
        this.longitud = longitud;
        this.latitud = latitud;
    	this.ruta = ruta;
        this.urlMiniatura = urlMiniatura;
    }
}
