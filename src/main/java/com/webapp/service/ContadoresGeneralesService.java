package com.webapp.service;

import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imageProcessor.dto.viajes.ContadorGeneralViajesDTO;
import com.imageProcessor.model.Viaje;
import com.imageProcessor.repository.ViajeRepository;
import com.webapp.controllers.ViajeController;

@Service
public class ContadoresGeneralesService {

	@Autowired 
	private ViajeRepository viajeRepository;
	
	private static final Logger log = LoggerFactory.getLogger(ViajeController.class);
	
	public ContadorGeneralViajesDTO buscarContadores (Long idUsuario) {
		
		try {
			log.info("Entramos a buscar contadores para el usuario: " + idUsuario);
			ContadorGeneralViajesDTO contadores = new ContadorGeneralViajesDTO();
			
			log.info("Llamamos a viajeRepository.viajesPorUsuario con el usuario: " + idUsuario);
			List<Viaje> viajes = viajeRepository.viajesPorUsuario(idUsuario);
			log.info("Recogemos los viajes: " + viajes.toString());
			Set<String> listaPaises = new LinkedHashSet<>();
			for (Viaje viaje:viajes) {
				if (viaje.getPais() != null && !viaje.getPais().isEmpty()) {
					String paises = viaje.getPais();
					String[] listaPaisesAux = paises.split(",");
					for (String pais:listaPaisesAux) {
						listaPaises.add(pais.trim());
					}
				}
				if (viaje.getFecha_inicio() != null && viaje.getFecha_fin() != null) {
					long tiempo = ChronoUnit.DAYS.between(viaje.getFecha_inicio().toLocalDate(), viaje.getFecha_fin().toLocalDate());
					if (viaje.getFecha_fin().getHour() > viaje.getFecha_inicio().getHour() || viaje.getFecha_fin().getMinute() > viaje.getFecha_inicio().getMinute()) {
			            tiempo++;
			        }
					int dias = (int) tiempo;
					contadores.setDias(contadores.getDias()+dias);
				}
				contadores.setViajes(contadores.getViajes()+1);
			}
			
			contadores.setContinentes(viajeRepository.continentesPorUsuario(idUsuario));
			contadores.setPaises(listaPaises.size());

			log.info("Devolvemos los contadores: " + contadores);
			return contadores;
		} catch (Exception e) {
			log.error("Error al sacar las estadísticas generales: " + e.getMessage());
			return null;
		}
	}
}
