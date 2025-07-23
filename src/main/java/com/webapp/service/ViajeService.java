package com.webapp.service;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imageProcessor.dto.pasos.VerPasoDto;
import com.imageProcessor.dto.viajes.ConsultarViajesDTO;
import com.imageProcessor.dto.viajes.CrearViajeDTO;
import com.imageProcessor.dto.viajes.ViajeDTO;
import com.imageProcessor.model.Paso;
import com.imageProcessor.model.Viaje;
import com.imageProcessor.repository.ImagenViajePasoRepository;
import com.imageProcessor.repository.PasoRepository;
import com.imageProcessor.repository.ViajeRepository;
import com.webapp.controllers.ViajeController;
import com.webapp.util.ConsultarViajesUtil;

@Service
public class ViajeService {

	@Autowired
	private ViajeRepository viajeRepository;
	
	@Autowired
	private PasoRepository pasoRepository;

	@Autowired
	private ImagenViajePasoRepository imagenViajePasoRepository;

	@Autowired
	private PasoService pasoService;

	private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
	
	private static final Logger log = LoggerFactory.getLogger(ViajeController.class);
	
	public CrearViajeDTO crearViaje (Long idUsuario, String viajeString) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		
		try {
			log.info("Entramos en crearViaje (Service) para el usuario " + idUsuario + " y el viaje " + viajeString);
			CrearViajeDTO viajeDTO = objectMapper.readValue(viajeString, CrearViajeDTO.class);
			log.info("Después de pasar el Json a CrearViajeDTO: " + viajeDTO);
			viajeDTO.setUsuario_id(idUsuario);

			Viaje viaje = new Viaje();
			viaje.setNombre(viajeDTO.getNombre());
			viaje.setDescripcion(viajeDTO.getDescripcion());
			viaje.setFecha_inicio(viajeDTO.getFecha_inicio());
			viaje.setFecha_fin(viajeDTO.getFecha_fin());
			viaje.setPais(viajeDTO.getPais());
			viaje.setContinente(viajeDTO.getContinente());
			viaje.setUsuario_id(viajeDTO.getUsuario_id());
			log.info("Antes de grabar el viaje: " + viaje.toString());
			Viaje viajeGrabado = viajeRepository.save(viaje);
			log.info("Después de grabar el viaje: " + viajeGrabado.getId());
			
			// Aquí falta el código para grabar si el viaje es privado o público -- FALTA
			log.info("Falta grabar viaje publico privado");
			// Devolvemos el control
			return viajeDTO;
		} catch (JsonProcessingException e) {
			log.error("Error al crear el viaje: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	public String eliminarViaje (Long idUsuario, Long id) {
		
		try {

			log.info("Entramos en eliminar (Service) para el usuario " + idUsuario + " y el viaje " + id);
			log.info("Entramos en eliminar fotos del viaje si existen");
			imagenViajePasoRepository.borrarImagenesPorViaje(id);
			log.info("Después de eliminar las imagenes del usuario " + idUsuario + " y el viaje " + id);
			
			log.info("Entramos en eliminar pasos del viaje si existen");
			pasoRepository.borrarPasosPorViaje(id);
			log.info("Después de eliminar los pasos del usuario " + idUsuario + " y el viaje " + id);
			
			log.info("Entramos en eliminar el viaje para el usuario " + idUsuario + " y el viaje " + id);
			viajeRepository.deleteById(id);
			log.info("Después de eliminar el viaje: " + id);
			
			// Devolvemos el control
			return "OK";
		} catch (Exception e) {
			log.error("Error al eliminar el viaje: " + e.getMessage());
			e.printStackTrace();
			return "KO";
		}
	}
	
	@Transactional
	public CrearViajeDTO actualizarViaje (Long idUsuario, String viajeString) { 
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		
		try {
			log.info("Entramos en actualizarViaje (Service) para el usuario " + idUsuario + " y el viaje " + viajeString);
			CrearViajeDTO viajeDTO = objectMapper.readValue(viajeString, CrearViajeDTO.class);
			log.info("Después de pasar el Json a CrearViajeDTO: " + viajeDTO);
			viajeDTO.setUsuario_id(idUsuario);

			Viaje viaje = new Viaje();
			viaje.setId(viajeDTO.getId());
			viaje.setNombre(viajeDTO.getNombre());
			viaje.setDescripcion(viajeDTO.getDescripcion());
			viaje.setFecha_inicio(viajeDTO.getFecha_inicio());
			viaje.setFecha_fin(viajeDTO.getFecha_fin());
			viaje.setPais(viajeDTO.getPais());
			viaje.setContinente(viajeDTO.getContinente());
			viaje.setUsuario_id(viajeDTO.getUsuario_id());
			log.info("Antes de actualizar el viaje: " + viaje.toString());
			Viaje viajeGrabado = viajeRepository.save(viaje);
			log.info("Después de actualizar el viaje: " + viajeGrabado.getId());
			
			// Aquí falta el código para grabar si el viaje es privado o público -- FALTA
			log.info("Falta grabar viaje publico privado");
			// Devolvemos el control
			return viajeDTO;
		} catch (JsonProcessingException e) {
			log.error("Error al crear el viaje: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
	public String consultarViajesGenerales (Long idUsuario) {
		
		try {
			log.info("Entramos a consultarViajesGenerales para el usuario: " + idUsuario);
			List<ConsultarViajesDTO> consultarViajes = new ArrayList<>();
			consultarViajes = consultarViajesGeneralesDTO(idUsuario);
			if (consultarViajes == null) {
				return null;
			}
			
			// Tenemos un listado con viajes y hay que pasarlo a Json
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			log.info("Antes de pasar viajes a Json: " + consultarViajes.toString());
			return objectMapper.writeValueAsString(consultarViajes);

		} catch (Exception e) {
			return null;
		}
		
	}
	
	public List<String> obtenerPaises (Long idUsuario){
		
		log.info("ntramos a obtenerPaises para el usuario: " + idUsuario);
		List<String> paisesUsuario = new ArrayList<>();
		try {
			log.info("Llamamos al repository viajeRepository.listaPaisesPorUsuario con el usuario: " + idUsuario);
			paisesUsuario = viajeRepository.listaPaisesPorUsuario(idUsuario); // llamada a BBDD para sacar los paises
		} catch (Exception e) {
			log.error("Error al buscar los paises para el usuario: " + idUsuario + ". Error: " + e.getMessage());
			return null;
		}
        log.info("Devolvemos los paises: " + paisesUsuario);
		return paisesUsuario;
	}

	public int validarUsuarioViaje(Long usuario, Long viaje) {
	
		try {
			log.info("Buscamos relacción usuario/viaje. Usuario: " + usuario + " y viaje: " + viaje);
			int encontrados = viajeRepository.validaViajeUsuario(viaje, usuario);
			if (encontrados==1) {
				log.info("Usuario encontrado para el viaje: " + usuario + " y viaje: " + viaje);
				return 0;
			}
			else {
				log.info("Usuario no encontrado para el viaje: " + usuario + " y viaje: " + viaje);
				return 1;
			}
		} catch (Exception e) {
			log.error("Error al validarUsuarioViaje para el usuario " + usuario + " y viaje " + viaje + e.getMessage());
			return 1;
		}
	}

	public ViajeDTO buscarViaje(Long id) {
		try {
			log.info("Entramos a buscar el viaje: " + id);
			ViajeDTO viajeDTO = new ViajeDTO();
			Viaje viaje = viajeRepository.viajesPorId(id);
			log.info("Viaje encontrado: " + viaje.getNombre());
			viajeDTO.setIdViaje(id);
			viajeDTO.setNombre(viaje.getNombre());
			viajeDTO.setDescripcion(viaje.getDescripcion());
			viajeDTO.setFechaInicio(viaje.getFecha_inicio());
		    viajeDTO.setFechaFin(viaje.getFecha_fin());
		    viajeDTO.setPaises(viaje.getPais());
		    viajeDTO.setContinente(viaje.getContinente());
		    List<VerPasoDto> verPasosDTO = pasoService.obtenerPasosDTO(id);
		    viajeDTO.setPasos(verPasosDTO);
		    log.info("Salida ViajeDTO: " + viajeDTO.toString());
			return viajeDTO;
		} catch (Exception e) {
			log.error("Error al obtener el viaje con id " + id + ". " + e.getMessage());
			return null;
		}
	}

	public List<ConsultarViajesDTO> consultarViajesGeneralesDTO (Long idUsuario) {
		
		try {
			log.info("Entramos a consultarViajesGenerales para el usuario: " + idUsuario);
			List<ConsultarViajesDTO> consultarViajes = new ArrayList<>();
			log.info("Llamamos a repository para sacar los viajes (viajeRepository.viajesPorUsuario)");
			List<Viaje> viajes = viajeRepository.viajesPorUsuario(idUsuario);
			if (viajes==null) {
				log.info("Sin viajes");
				return null;
			}
			log.info("Con viajes");

			for (Viaje viaje:viajes) {
				log.info("Viaje recibido: " + viaje.getId());
				ConsultarViajesDTO consultarViaje = new ConsultarViajesDTO();
				
				try {
					log.info("Calculamos los días del viaje: " + viaje.getId());
					consultarViaje.setDias(ConsultarViajesUtil.calcularDiasViaje(viaje));
				} catch (Exception e) {
					log.error("Error al calcular los días del viaje. Ponemos 0. Viaje: " + viaje.getId());
					consultarViaje.setDias(0);
					e.printStackTrace();
				}
				
				try {
					log.info("Buscamos los pasos del viaje: " + viaje.getId());
					List<Paso> pasos = new ArrayList<>();
					pasos = pasoRepository.pasosPorViaje(viaje.getId());
					log.info("Pasos recuperados");
					long km = 0;
					int contPasos = 0;
					Point puntoCasa = geometryFactory.createPoint(new Coordinate(-3.7038, 40.4168));
					Point puntoAnt =  geometryFactory.createPoint(new Coordinate(-3.7038, 40.4168));
					for (Paso paso:pasos) {
						contPasos++;
						log.info("Llamamos a calcular distancia para el paso: " + paso.getId() + " del viaje: " + viaje.getId());
						km = km + ConsultarViajesUtil.calcularKmPaso(paso.getCoordenadas(),puntoAnt);
						puntoAnt = paso.getCoordenadas();
					}
					
					// Después del último paso queda la vuelta a casa
					km = km + ConsultarViajesUtil.calcularKmPaso(puntoAnt,puntoCasa);
					
					log.info("Construimos la salida del viaje: " + viaje.getId());
					consultarViaje.setIdViaje(viaje.getId());
					consultarViaje.setKilometros(km);
					consultarViaje.setContinente(viaje.getContinente());
					consultarViaje.setFechaInicio(viaje.getFecha_inicio());
					consultarViaje.setFechaFin(viaje.getFecha_fin());
					consultarViaje.setNombre(viaje.getNombre());
					consultarViaje.setPais(viaje.getPais());
					consultarViaje.setPasos(contPasos);
				} catch (Exception e) {
					log.error("Error al consultar el viaje: " + viaje.getId() + ". Error: " + e.getMessage());
					return null;
				}
				
				log.info("Añadimos el viaje a la salida: " + viaje.getId());
				consultarViajes.add(consultarViaje);
			}
			return consultarViajes;

		} catch (Exception e) {
			return null;
		}
		
	}
	
}
