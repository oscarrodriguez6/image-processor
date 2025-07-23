package com.imageProcessor.dto.viajes;

import java.time.LocalDateTime;

public class ConsultarViajesDTO {

	private Long idViaje;
    private String nombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String pais;
    private String continente;
    private int dias;
    private long kilometros;
    private int pasos;

    public ConsultarViajesDTO(Long idViaje, String nombre, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String pais, String continente, int dias, long kilometros, int pasos) {
		super();
		this.idViaje = idViaje;
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.pais = pais;
		this.continente = continente;
		this.dias = dias;
		this.kilometros = kilometros;
		this.pasos = pasos;
	}

	public ConsultarViajesDTO() {
		super();
	}
	
    public Long getIdViaje() {
		return idViaje;
	}

	public void setIdViaje(Long idViaje) {
		this.idViaje = idViaje;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public LocalDateTime getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getContinente() {
		return continente;
	}
	public void setContinente(String continente) {
		this.continente = continente;
	}
	public int getDias() {
		return dias;
	}
	public void setDias(int dias) {
		this.dias = dias;
	}
	public long getKilometros() {
		return kilometros;
	}
	public void setKilometros(long kilometros) {
		this.kilometros = kilometros;
	}
	public int getPasos() {
		return pasos;
	}
	public void setPasos(int pasos) {
		this.pasos = pasos;
	}

}
