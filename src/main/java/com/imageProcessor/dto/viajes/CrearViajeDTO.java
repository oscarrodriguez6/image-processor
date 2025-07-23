package com.imageProcessor.dto.viajes;

import java.time.LocalDateTime;

public class CrearViajeDTO {

	private Long id;
	
	private String nombre;
	private String descripcion;
	private LocalDateTime fecha_inicio;
	private LocalDateTime fecha_fin;
	private String pais;
	private String continente;
	private Long usuario_id;
	private String visibilidad;
	public CrearViajeDTO(Long id, String nombre, String descripcion, LocalDateTime fecha_inicio,
			LocalDateTime fecha_fin, String pais, String continente, Long usuario_id, String visibilidad) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.pais = pais;
		this.continente = continente;
		this.usuario_id = usuario_id;
		this.visibilidad = visibilidad;
	}
	public CrearViajeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public LocalDateTime getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(LocalDateTime fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	public LocalDateTime getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(LocalDateTime fecha_fin) {
		this.fecha_fin = fecha_fin;
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
	public Long getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}
	public String getVisibilidad() {
		return visibilidad;
	}
	public void setVisibilidad(String visibilidad) {
		this.visibilidad = visibilidad;
	}
		

}
