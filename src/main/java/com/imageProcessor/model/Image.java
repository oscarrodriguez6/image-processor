package com.imageProcessor.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="image")
public class Image {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String ruta;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // Aquí se especifica el nombre correcto
	private Usuario usuario;
	
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
	private Map<String, Object> metadata = new HashMap<>();
    
    @JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private Map<String, Object> etiquetas = new HashMap<>();
    
	private Boolean procesada;
	private Boolean procesadaIA;
	private LocalDateTime fecha;
	private String thumbnailUrl;

	@Column(columnDefinition = "geometry(Point, 4326)")
	private Point coordenadas;

	public Image() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Image(Long id, String ruta, Usuario usuario, Map<String, Object> metadata, Map<String, Object> etiquetas,
			Boolean procesada, Boolean procesadaIA, LocalDateTime fecha, String thumbnailUrl, Point coordenadas) {
		super();
		this.id = id;
		this.ruta = ruta;
		this.usuario = usuario;
		this.metadata = metadata;
		this.etiquetas = etiquetas;
		this.procesada = procesada;
		this.procesadaIA = procesadaIA;
		this.fecha = fecha;
		this.thumbnailUrl = thumbnailUrl;
		this.coordenadas = coordenadas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	public Map<String, Object> getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(Map<String, Object> etiquetas) {
		this.etiquetas = etiquetas;
	}

	public Boolean getProcesada() {
		return procesada;
	}

	public void setProcesada(Boolean procesada) {
		this.procesada = procesada;
	}

	public Boolean getProcesadaIA() {
		return procesadaIA;
	}

	public void setProcesadaIA(Boolean procesadaIA) {
		this.procesadaIA = procesadaIA;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public Point getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(Point coordenadas) {
		this.coordenadas = coordenadas;
	}

	@Override
	public String toString() {
		return "Image [id=" + id + ", ruta=" + ruta + ", usuario=" + usuario + ", metadata=" + metadata + ", etiquetas="
				+ etiquetas + ", procesada=" + procesada + ", procesadaIA=" + procesadaIA + ", fecha=" + fecha
				+ ", thumbnailUrl=" + thumbnailUrl + ", coordenadas=" + coordenadas + "]";
	}

}
