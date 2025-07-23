package com.imageProcessor.model;

import java.time.LocalDateTime;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imageProcessor.utils.PointSerializer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "paso")
public class Paso {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String descripcion;
	private LocalDateTime fecha;
	
	@Column(columnDefinition = "geometry(Point, 4326)")
	@JsonSerialize(using = PointSerializer.class)
	private Point coordenadas;
	
	private String pais;
	private String continente;
	private Long viaje_id;

	public Paso(Long id, String nombre, String descripcion, LocalDateTime fecha, Point coordenadas, String pais,
			String continente, Long viaje_id) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.coordenadas = coordenadas;
		this.pais = pais;
		this.continente = continente;
		this.viaje_id = viaje_id;
	}

	public Paso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public double getLongitud() {
		return coordenadas.getX(); // X = longitud
	}

	public double getLatitud() {
		return coordenadas.getY(); // Y = latitud
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

	@Override
	public String toString() {
		return "Paso [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", fecha=" + fecha
				+ ", coordenadas=" + coordenadas + ", pais=" + pais + ", continente=" + continente + ", viaje_id="
				+ viaje_id + "]";
	}
	

}
