package com.webapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imageProcessor.dto.viajes.ConsultarViajesDTO;
import com.imageProcessor.dto.viajes.ContadorCompletosViajesDTO;
import com.imageProcessor.dto.viajes.ContadorGeneralViajesDTO;
import com.webapp.controllers.EstadisticasController;

@Service
public class EstadisticasService {

	@Autowired
	private ContadoresGeneralesService contadoresGeneralesService;
	
	@Autowired
	private ViajeService viajeService;
	
	private static final Logger log = LoggerFactory.getLogger(EstadisticasController.class);
	
	private int PAISES_TOTALES = 141;
	private int CONTINENTES_TOTALES = 5;
		
	public String obtenerEstadisticas(Long idUsuario) {
		
		try {
			log.info("Entramos a obtenerEstadisticas para el usuario: " + idUsuario);
			ContadorGeneralViajesDTO contadoresGenerales = contadoresGeneralesService.buscarContadores(idUsuario);
			
			log.info("Entramos a buscarContadoresCompletos" + idUsuario);
			ContadorCompletosViajesDTO contadoresCompletos = buscarContadoresCompletos(idUsuario, contadoresGenerales);
			
			// Tenemos un listado con viajes y hay que pasarlo a Json
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			log.info("Antes de pasar viajes a Json: " + contadoresCompletos.toString());
			return objectMapper.writeValueAsString(contadoresCompletos);

		} catch (Exception e) {
			return null;
		}

	}
	
	private ContadorCompletosViajesDTO buscarContadoresCompletos(Long idUsuario, ContadorGeneralViajesDTO contadoresGenerales) {
		
		ContadorCompletosViajesDTO contadores = new ContadorCompletosViajesDTO();

		// Contadores generales
		log.info("Rellenamos contadores generales");
		contadores.setContadoresGenerales(contadoresGenerales);
		log.info("Rellenamos porcentajes");
		if (contadoresGenerales.getPaises()>0) {
			contadores.setPorcentajePaises((double) contadoresGenerales.getPaises() * 100 / PAISES_TOTALES);
		} else {
			contadores.setPorcentajePaises((double) 0);
		}
		if (contadoresGenerales.getContinentes() > 0) {
			contadores.setPorcentajeContinentes((double) contadoresGenerales.getContinentes() * 100 / CONTINENTES_TOTALES);
		} else {
			contadores.setPorcentajeContinentes((double) 0);
		}
		
		log.info("Sacamos la lista de viajes");
		List<ConsultarViajesDTO> listaViajes = new ArrayList<>();
		listaViajes = viajeService.consultarViajesGeneralesDTO(idUsuario);
		double km = 0;
		String paisesSinTratar = "";
		
		log.info("Recorremos la lista de viajes");
		if (!listaViajes.isEmpty()) {
			log.info("Encontramos viajes");

			for (ConsultarViajesDTO viaje:listaViajes) {
				log.info("Por cada viaje: " + viaje);
				
				km =+viaje.getKilometros();
				
				if (viaje.getPais() != null && !viaje.getPais().isEmpty()){
					if (paisesSinTratar.equals("")) {
						paisesSinTratar = viaje.getPais();
					} else {
						paisesSinTratar = paisesSinTratar + "," + (viaje.getPais());
					}
				}
				if (viaje.getDias() > contadores.getViajeMasLargo().getDias()) {
					contadores.getViajeMasLargo().setDias(viaje.getDias());
					contadores.getViajeMasLargo().setNombre(viaje.getNombre());
				}
				
				if (contadores.getViajeMasReciente().getFecha() == null || viaje.getFechaFin().isAfter(contadores.getViajeMasReciente().getFecha())) {
					contadores.getViajeMasReciente().setFecha(viaje.getFechaFin());
					contadores.getViajeMasReciente().setNombre(viaje.getNombre());
				}
				
			}
		}
		contadores.setKmRecorridos(km);
		contadores.setBanderas(Arrays.stream(paisesSinTratar.split(",")).distinct().toArray(String[]::new));
		
		return contadores;
	}
}
