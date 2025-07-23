package com.imageProcessor.dto.viajes;

public class ContadorGeneralViajesDTO {
	
	private int viajes;
	private int paises;
	private int continentes;
	private int dias;

	public ContadorGeneralViajesDTO(int viajes, int paises, int continentes, int dias) {
		super();
		this.viajes = viajes;
		this.paises = paises;
		this.continentes = continentes;
		this.dias = dias;
	}
	
	public ContadorGeneralViajesDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getViajes() {
		return viajes;
	}
	public void setViajes(int viajes) {
		this.viajes = viajes;
	}
	public int getPaises() {
		return paises;
	}
	public void setPaises(int paises) {
		this.paises = paises;
	}
	public int getContinentes() {
		return continentes;
	}
	public void setContinentes(int continentes) {
		this.continentes = continentes;
	}
	public int getDias() {
		return dias;
	}
	public void setDias(int dias) {
		this.dias = dias;
	}

	
}
