package com.imageProcessor.dto;

public class ImageDTO {

    private String nombre;
    private String urlMiniatura;
    private Long idOriginal;
    private String fechaLateral;

    public ImageDTO(String nombre, String urlMiniatura, Long idOriginal, String fechaLateral) {
        this.nombre = nombre;
        this.urlMiniatura = urlMiniatura;
        this.idOriginal = idOriginal;
        this.fechaLateral = fechaLateral;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrlMiniatura() {
        return urlMiniatura;
    }

    public Long getIdOriginal() {
        return idOriginal;
    }

	public String getFechaLateral() {
		return fechaLateral;
	}

}
