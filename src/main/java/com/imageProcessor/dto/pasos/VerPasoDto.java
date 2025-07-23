package com.imageProcessor.dto.pasos;

import java.time.LocalDateTime;
import java.util.List;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imageProcessor.dto.ImageDTO;
import com.imageProcessor.model.Paso;
import com.imageProcessor.utils.PointSerializer;

public class VerPasoDto {

	private Long id;
	private String nombre;
	private String descripcion;
	private LocalDateTime fecha;
	@JsonSerialize(using = PointSerializer.class)
	private Point coordenadas;
	private String pais;
	private String continente;
	private Long viaje_id;
	private List<ImageDTO> imagenes;
	private Double longitud;
	private Double latitud;
	
	public VerPasoDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VerPasoDto(Long id, String nombre, String descripcion, LocalDateTime fecha, Point coordenadas, String pais,
			String continente, Long viaje_id, List<ImageDTO> imagenes, Double longitud, Double latitud) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.coordenadas = coordenadas;
		this.pais = pais;
		this.continente = continente;
		this.viaje_id = viaje_id;
		this.imagenes = imagenes;
		this.longitud = longitud;
		this.latitud = latitud;
	}

	public VerPasoDto(Paso paso, List<ImageDTO> imagenes) {
		super();
		this.id = paso.getId();
		this.nombre = paso.getNombre();
		this.descripcion = paso.getDescripcion();
		this.fecha = paso.getFecha();
		this.coordenadas = paso.getCoordenadas();
		this.pais = paso.getPais();
		this.continente = paso.getContinente();
		this.viaje_id = paso.getViaje_id();
		this.imagenes = imagenes;
		this.longitud= paso.getCoordenadas().getX();
		this.latitud = paso.getCoordenadas().getY();
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

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Point getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(Point coordenadas) {
		this.coordenadas = coordenadas;
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

	public Long getViaje_id() {
		return viaje_id;
	}

	public void setViaje_id(Long viaje_id) {
		this.viaje_id = viaje_id;
	}

	public List<ImageDTO> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<ImageDTO> imagenes) {
		this.imagenes = imagenes;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

}
