package com.imageProcessor.dto.viajes;

import java.time.LocalDateTime;

public class ContadorCompletosViajesDTO {
	
	public class ViajeDuracion {
		private String nombre;
		private int dias;
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public int getDias() {
			return dias;
		}
		public void setDias(int dias) {
			this.dias = dias;
		}
		public ViajeDuracion() {
			super();
			// TODO Auto-generated constructor stub
		}
		public ViajeDuracion(String nombre, int dias) {
			super();
			this.nombre = nombre;
			this.dias = dias;
		}
		
	}
	
	public class ViajeFecha {
		private String nombre;
		private LocalDateTime fecha;
		public ViajeFecha(String nombre, LocalDateTime fecha) {
			super();
			this.nombre = nombre;
			this.fecha = fecha;
		}
		public ViajeFecha() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public LocalDateTime getFecha() {
			return fecha;
		}
		public void setFecha(LocalDateTime fecha) {
			this.fecha = fecha;
		}
		
	}
	
	ContadorGeneralViajesDTO contadoresGenerales;
	private Double porcentajePaises;
	private Double porcentajeContinentes;
	private Double KmRecorridos;
	private String[] banderas;
	private ViajeDuracion viajeMasLargo;
	private ViajeFecha viajeMasReciente;
	
	public ContadorCompletosViajesDTO() {
		super();
		ViajeDuracion viajeMasLargo = new ViajeDuracion("",0);
		this.viajeMasLargo = viajeMasLargo;
		ViajeFecha viajeMasReciente = new ViajeFecha("",null);
		this.viajeMasReciente = viajeMasReciente;
	}

	public ContadorCompletosViajesDTO(ContadorGeneralViajesDTO contadoresGenerales, Double porcentajePaises,
			Double porcentajeContinentes, Double kmRecorridos, String[] banderas, ViajeDuracion viajeMasLargo,
			ViajeFecha viajeMasReciente) {
		super();
		this.contadoresGenerales = contadoresGenerales;
		this.porcentajePaises = porcentajePaises;
		this.porcentajeContinentes = porcentajeContinentes;
		KmRecorridos = kmRecorridos;
		this.banderas = banderas;
		this.viajeMasLargo = viajeMasLargo;
		this.viajeMasReciente = viajeMasReciente;
	}

	public ContadorGeneralViajesDTO getContadoresGenerales() {
		return contadoresGenerales;
	}

	public void setContadoresGenerales(ContadorGeneralViajesDTO contadoresGenerales) {
		this.contadoresGenerales = contadoresGenerales;
	}

	public Double getPorcentajePaises() {
		return porcentajePaises;
	}

	public void setPorcentajePaises(Double porcentajePaises) {
		this.porcentajePaises = porcentajePaises;
	}

	public Double getPorcentajeContinentes() {
		return porcentajeContinentes;
	}

	public void setPorcentajeContinentes(Double porcentajeContinentes) {
		this.porcentajeContinentes = porcentajeContinentes;
	}

	public Double getKmRecorridos() {
		return KmRecorridos;
	}

	public void setKmRecorridos(Double kmRecorridos) {
		KmRecorridos = kmRecorridos;
	}

	public String[] getBanderas() {
		return banderas;
	}

	public void setBanderas(String[] banderas) {
		this.banderas = banderas;
	}

	public ViajeDuracion getViajeMasLargo() {
		return viajeMasLargo;
	}

	public void setViajeMasLargo(ViajeDuracion viajeMasLargo) {
		this.viajeMasLargo = viajeMasLargo;
	}

	public ViajeFecha getViajeMasReciente() {
		return viajeMasReciente;
	}

	public void setViajeMasReciente(ViajeFecha viajeMasReciente) {
		this.viajeMasReciente = viajeMasReciente;
	}
	
}
