package com.imageProcessor.dto;

public class CargaMasivaResponse {
    private String usuario;
    private String carpeta;
    private int opcionTratamiento;
    private int imagenesProcesadas;
    private int imagenesIgnoradas;
    private int imagenesError;
    private String mensaje;
    private String tiempo;
    
    public CargaMasivaResponse(String usuario, String carpeta, int opcionTratamiento, int imagenesProcesadas,
			int imagenesIgnoradas, int imagenesError, String mensaje, String tiempo) {
		super();
		this.usuario = usuario;
		this.carpeta = carpeta;
		this.opcionTratamiento = opcionTratamiento;
		this.imagenesProcesadas = imagenesProcesadas;
		this.imagenesIgnoradas = imagenesIgnoradas;
		this.imagenesError = imagenesError;
		this.mensaje = mensaje;
		this.tiempo = tiempo;
	}
    
    public CargaMasivaResponse(String usuario, String carpeta, int opcionTratamiento, int imagenesProcesadas,
			int imagenesIgnoradas, int imagenesError, String mensaje, Long duracion) {
		super();
		this.usuario = usuario;
		this.carpeta = carpeta;
		this.opcionTratamiento = opcionTratamiento;
		this.imagenesProcesadas = imagenesProcesadas;
		this.imagenesIgnoradas = imagenesIgnoradas;
		this.imagenesError = imagenesError;
		this.mensaje = mensaje;
        // Convertir a un formato legible
        long segundos = duracion / 1_000_000_000;
        long minutos = segundos / 60;
        long horas = minutos / 60;
        segundos = segundos % 60;
        minutos = minutos % 60;
        long decimas = (duracion / 100_000_000) % 10; // Décimas de segundo

		this.tiempo = "El método tardó " + horas + " horas, " + minutos + " minutos, " + segundos + " segundos y " + decimas + "décimas de segundo en ejecutarse";
	}
	
	public CargaMasivaResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getCarpeta() {
		return carpeta;
	}
	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}
	public int getOpcionTratamiento() {
		return opcionTratamiento;
	}
	public void setOpcionTratamiento(int opcionTratamiento) {
		this.opcionTratamiento = opcionTratamiento;
	}
	public int getImagenesProcesadas() {
		return imagenesProcesadas;
	}
	public void setImagenesProcesadas(int imagenesProcesadas) {
		this.imagenesProcesadas = imagenesProcesadas;
	}
	public int getImagenesIgnoradas() {
		return imagenesIgnoradas;
	}
	public void setImagenesIgnoradas(int imagenesIgnoradas) {
		this.imagenesIgnoradas = imagenesIgnoradas;
	}
	public int getImagenesError() {
		return imagenesError;
	}
	public void setImagenesError(int imagenesError) {
		this.imagenesError = imagenesError;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}


}
