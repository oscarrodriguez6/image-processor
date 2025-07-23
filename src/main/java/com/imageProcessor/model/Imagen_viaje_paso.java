package com.imageProcessor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "imagen_viaje_paso")
public class Imagen_viaje_paso {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long imagen_id;
	private Long viaje_id;
	private Long paso_id;

	public Imagen_viaje_paso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Imagen_viaje_paso(Long id, Long imagen_id, Long viaje_id, Long paso_id) {
		super();
		this.id = id;
		this.imagen_id = imagen_id;
		this.viaje_id = viaje_id;
		this.paso_id = paso_id;
	}

	public Imagen_viaje_paso(Long imagen_id, Long viaje_id, Long paso_id) {
		super();
		this.imagen_id = imagen_id;
		this.viaje_id = viaje_id;
		this.paso_id = paso_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getImagen_id() {
		return imagen_id;
	}

	public void setImagen_id(Long imagen_id) {
		this.imagen_id = imagen_id;
	}

	public Long getViaje_id() {
		return viaje_id;
	}

	public void setViaje_id(Long viaje_id) {
		this.viaje_id = viaje_id;
	}

	public Long getPaso_id() {
		return paso_id;
	}

	public void setPaso_id(Long paso_id) {
		this.paso_id = paso_id;
	}


}
