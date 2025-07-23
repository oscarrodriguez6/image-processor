package com.imageProcessor.dto.viajes;

import java.time.LocalDateTime;
import java.util.List;

import com.imageProcessor.dto.pasos.VerPasoDto;

public class ViajeDTO {

	private Long idViaje;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String paises;
    private String continente;
    private String propietario;
    private boolean visible;
    private List<VerPasoDto> pasos;
    
    
	public ViajeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ViajeDTO(Long idViaje, String nombre, String descripcion, LocalDateTime fechaInicio, LocalDateTime fechaFin,
			String paises, String continente, String propietario, boolean visible, List<VerPasoDto> pasos) {
		super();
		this.idViaje = idViaje;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.paises = paises;
		this.continente = continente;
		this.propietario = propietario;
		this.visible = visible;
		this.pasos = pasos;
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


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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


	public String getPaises() {
		return paises;
	}


	public void setPaises(String paises) {
		this.paises = paises;
	}


	public String getContinente() {
		return continente;
	}


	public void setContinente(String continente) {
		this.continente = continente;
	}


	public String getPropietario() {
		return propietario;
	}


	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}


	public boolean isVisible() {
		return visible;
	}


	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	public List<VerPasoDto> getPasos() {
		return pasos;
	}


	public void setPasos(List<VerPasoDto> pasos) {
		this.pasos = pasos;
	}

}