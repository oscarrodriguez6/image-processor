package com.imageProcessor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarioPais")
public class UsuarioPais {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long viaje_id;
	private Long usuario_id;
	private String pais;
	private String continente;
	public UsuarioPais(Long id, Long viaje_id, Long usuario_id, String pais, String continente) {
		super();
		this.id = id;
		this.viaje_id = viaje_id;
		this.usuario_id = usuario_id;
		this.pais = pais;
		this.continente = continente;
	}
	public UsuarioPais() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getViaje_id() {
		return viaje_id;
	}
	public void setViaje_id(Long viaje_id) {
		this.viaje_id = viaje_id;
	}
	public Long getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
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
	
	
	

}
