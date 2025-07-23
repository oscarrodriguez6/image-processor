package com.webapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imageProcessor.dto.ImageDTO;
import com.imageProcessor.dto.pasos.CrearPasoDTO;
import com.imageProcessor.dto.pasos.EditarPasoDTO;
import com.imageProcessor.dto.pasos.VerPasoDto;
import com.imageProcessor.model.Image;
import com.imageProcessor.model.Imagen_viaje_paso;
import com.imageProcessor.model.Paso;
import com.imageProcessor.repository.ImageRepository;
import com.imageProcessor.repository.ImagenViajePasoRepository;
import com.imageProcessor.repository.PasoRepository;
import com.webapp.controllers.PasoController;

@Service
public class PasoService {

	@Autowired
	private PasoRepository pasoRepository;

	@Autowired
	private ImagenViajePasoRepository imagenViajeRepository;

	private static final Logger log = LoggerFactory.getLogger(PasoController.class);
	
    @Autowired
    private ImageRepository imagenRepository;
	
	public String crearPaso (Long idUsuario, CrearPasoDTO crearPasoDTO) {
		
		try {
			log.info("Entramos a crear un paso: " + crearPasoDTO.toString());
			Paso paso = new Paso ();
			paso.setNombre(crearPasoDTO.getNombre());
			paso.setDescripcion(crearPasoDTO.getDescripcion());
			paso.setFecha(crearPasoDTO.getFecha());
			if (crearPasoDTO.getUbicacion() != null && !crearPasoDTO.getUbicacion().isEmpty()) {
				paso.setCoordenadas(crearPasoDTO.getUbicacion());
			}
			paso.setPais(crearPasoDTO.getPais());
			paso.setContinente(crearPasoDTO.getContinente());
			paso.setViaje_id(crearPasoDTO.getViaje_id());
			log.info("Paso a grabar: " + paso.toString());
			Paso pasoGrabado = pasoRepository.save(paso);
			log.info("Paso grabado: " + pasoGrabado.toString());
			
			for (Long imagen : crearPasoDTO.getImagenesIds()) {
				log.info("Imagen a grabar: " + imagen.toString());
				Imagen_viaje_paso imagenPaso = new Imagen_viaje_paso(imagen, pasoGrabado.getViaje_id(), pasoGrabado.getId());
				Imagen_viaje_paso imagenPasoGrabada = imagenViajeRepository.save(imagenPaso);
				log.info("Imagen grabada: " + imagenPasoGrabada.toString());
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			log.info("Salida. Antes de pasar a json: " + paso);
			return objectMapper.writeValueAsString(paso);
		} catch (Exception e) {
			log.error("Error al crear el paso: " + e.getMessage());
			return null;
		}
	}

	@Transactional
	public String actualizarPaso (Long idUsuario, EditarPasoDTO editarPasoDTO, Long idPaso) {
		
		try {
			log.info("Entramos a actualizar un paso: " + editarPasoDTO.toString());
			Paso paso = new Paso ();
			paso.setNombre(editarPasoDTO.getNombre());
			paso.setDescripcion(editarPasoDTO.getDescripcion());
			paso.setFecha(editarPasoDTO.getFecha());
			if (editarPasoDTO.getUbicacion() != null && !editarPasoDTO.getUbicacion().isEmpty()) {
				paso.setCoordenadas(editarPasoDTO.getUbicacion());
			}
			paso.setPais(editarPasoDTO.getPais());
			paso.setContinente(editarPasoDTO.getContinente());
			paso.setViaje_id(editarPasoDTO.getViaje_id());
			paso.setId(editarPasoDTO.getIdPaso());
			log.info("Antes de actualizar el paso: " + paso.toString());
			Paso pasoGrabado = pasoRepository.save(paso);
			log.info("Después de grabar el paso: " + pasoGrabado.toString());
			
			// Comprobamos si hay iimagenes
			int hayImagenes = imagenViajeRepository.contarImagenesPorPaso(paso.getViaje_id(),paso.getId());
			
			if (hayImagenes > 0) {
				// Borramos todas las imágenes asociadas a ese paso
				imagenViajeRepository.borrarImagenesPorPaso(paso.getViaje_id(),paso.getId());
				log.info("Imagenes borradas");
			}
			
			// Se dan de alta las nuevas imágenes
			for (Long imagen : editarPasoDTO.getImagenesIds()) {
				log.info("Imagen a grabar: " + imagen.toString());
				Imagen_viaje_paso imagenPaso = new Imagen_viaje_paso(imagen, pasoGrabado.getViaje_id(), pasoGrabado.getId());
				log.info("Imagen a grabar formateada: " + imagenPaso.toString());
				Imagen_viaje_paso imagenPasoGrabada = imagenViajeRepository.save(imagenPaso);
				log.info("Imagen grabada: " + imagenPasoGrabada.toString());
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			log.info("Salida. Antes de pasar a json: " + paso);
			return objectMapper.writeValueAsString(paso);
		} catch (Exception e) {
			log.error("Error al actualizar el paso: " + e.getMessage());
			return null;
		}
	}

	public List<VerPasoDto> obtenerPasosDTO(Long id) {
		log.info("Entramos a obtener los pasos para el viaje: " + id);
		List<VerPasoDto> listaPasosDTO = new ArrayList<>();
		List<Paso> listaPasos = pasoRepository.pasosPorViaje(id);

		log.info("Recuperamos los pasos: " + listaPasos.toString());
		for (Paso paso : listaPasos) {
			log.info("En el paso " + paso.getId() + " del viaje " + id + " tenemos los siguientes datos: " + paso.toString());
			List<Imagen_viaje_paso> listaImagenesPaso = imagenViajeRepository.buscarImagenesPorPaso(id, paso.getId());
			log.info("Después de buscar las imágenes: " + listaImagenesPaso.toString());
			List<ImageDTO> listaImagenesSalida = new ArrayList<>();
		
			for (Imagen_viaje_paso imagenPaso : listaImagenesPaso) {
				log.info("Tratamos la imagen: " + imagenPaso.toString());
				Optional<Image> imagenOpt = imagenRepository.findById(imagenPaso.getImagen_id());
				if (imagenOpt.isPresent()) {
					Image imagen = imagenOpt.get();
					log.info("Después de recuperar la imagen: " + imagen.toString());
					ImageDTO imagenDTO = new ImageDTO(imagen.getRuta(), imagen.getThumbnailUrl(), imagen.getId(), null);
					log.info("Mapeando la imagen: " + imagenDTO);
					listaImagenesSalida.add(imagenDTO);
					log.info("Se añade a la salida: " + listaImagenesSalida.toString());
				} else {
					log.info("No se han encontrado imagenes");
				}
			}
			
			log.info("Antes de añadir el paso");
			VerPasoDto verPasoDTO = new VerPasoDto(paso, listaImagenesSalida);
			log.info("Después de crear verPasoDTO: " + verPasoDTO);
			listaPasosDTO.add(verPasoDTO);
			log.info("Después de añadir el paso: " + listaPasosDTO);
		}
		log.info("Salimos con el listado: " + listaPasosDTO);
		return listaPasosDTO;
	}
	
	

}
