package com.imageProcessor.dto.pasos;

import java.time.LocalDateTime;
import java.util.List;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.imageProcessor.utils.PointDeserializer;

public class EditarPasoDTO {
	
	private Long idPaso;
	private Long viaje_id;
	private String nombre;
	private String descripcion;
	private LocalDateTime fecha;
	@JsonDeserialize(using = PointDeserializer.class)
	private Point ubicacion;
	private String pais;
	private String continente;
	private String poi;
	private List<Long> imagenesIds;

	public EditarPasoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EditarPasoDTO(Long idPaso, Long viaje_id, String nombre, String descripcion, LocalDateTime fecha,
			Point ubicacion, String pais, String continente, String poi, List<Long> imagenesIds) {
		super();
		this.idPaso = idPaso;
		this.viaje_id = viaje_id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.ubicacion = ubicacion;
		this.pais = pais;
		this.continente = continente;
		this.poi = poi;
		this.imagenesIds = imagenesIds;
	}

	public Long getIdPaso() {
		return idPaso;
	}

	public void setIdPaso(Long idPaso) {
		this.idPaso = idPaso;
	}

	public Long getViaje_id() {
		return viaje_id;
	}

	public void setViaje_id(Long viaje_id) {
		this.viaje_id = viaje_id;
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

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Point getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Point ubicacion) {
		this.ubicacion = ubicacion;
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

	public String getPoi() {
		return poi;
	}

	public void setPoi(String poi) {
		this.poi = poi;
	}

	public List<Long> getImagenesIds() {
		return imagenesIds;
	}

	public void setImagenesIds(List<Long> imagenesIds) {
		this.imagenesIds = imagenesIds;
	}

}
