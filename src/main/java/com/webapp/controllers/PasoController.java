package com.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imageProcessor.dto.pasos.CrearPasoDTO;
import com.imageProcessor.dto.pasos.EditarPasoDTO;
import com.imageProcessor.model.Usuario;
import com.imageProcessor.service.UsuarioService;
import com.webapp.service.PasoService;
import com.webapp.service.ViajeService;

@RestController
public class PasoController {
	
	private static final Logger log = LoggerFactory.getLogger(PasoController.class);

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ViajeService viajeService;
	
	@Autowired
	private PasoService pasoService;
	
	@PostMapping("/nuevoPaso")
	public ResponseEntity<String> nuevoPaso(@AuthenticationPrincipal UserDetails userDetails, @RequestBody String pasoJson){
        Usuario usuario = null;
        
        log.info("Entramos a crear un paso : " + pasoJson);
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Validamos el usuario : " + usuario.getEmail());
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			
			log.info("Recuperamos el json: " + pasoJson);
			CrearPasoDTO crearPaso = objectMapper.readValue(pasoJson, CrearPasoDTO.class);
			log.info("CrearPasoDTO generado: " + crearPaso);
			int resultado = validarDatosDTO(crearPaso);
			if (resultado == 1) throw new IllegalArgumentException("Datos incorrectos");
			
			resultado = viajeService.validarUsuarioViaje(usuario.getId(),crearPaso.getViaje_id());
			if (resultado == 1) {
				log.error("Viaje no se corresponde con el usuario: " + usuario.getId() + ". Viaje: " + crearPaso.getViaje_id());
				throw new IllegalArgumentException("Viaje no se corresponde con el usuario");
			}

			String paso = pasoService.crearPaso(usuario.getId(),crearPaso);
			if (paso == null || paso.isEmpty()) {
				log.error("Error al crear el paso: paso no creado (null)");
				return ResponseEntity.internalServerError().build();
			}
			log.info("Salimos con: " + paso.toString());
			return ResponseEntity.ok().body(paso);
		} catch (Exception e) { 
			log.error("Error al crear el paso: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
		
	}
	
	@PutMapping("/actualizarPaso/{idPaso}")
	public ResponseEntity<String> actualizarPaso(@AuthenticationPrincipal UserDetails userDetails, @RequestBody String pasoJson, @PathVariable Long idPaso){
        Usuario usuario = null;
        log.info("Entrada a actualizar paso: " + pasoJson);
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Validamos el usuario: " + usuario.getEmail());
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			
			EditarPasoDTO editarPaso = objectMapper.readValue(pasoJson, EditarPasoDTO.class);
			log.info("paso en json: " + pasoJson);
			int resultado = validarDatosPasoDTO(editarPaso);
			if (resultado == 1) throw new IllegalArgumentException("Datos incorrectos");
			
			resultado = viajeService.validarUsuarioViaje(usuario.getId(),editarPaso.getViaje_id());
			if (resultado == 1) throw new IllegalArgumentException("Viaje no se corresponde con el usuario");

			String paso = pasoService.actualizarPaso(usuario.getId(),editarPaso,idPaso);
			if (paso == null || paso.isEmpty()) {
				log.error("Error al crear el paso: paso no creado (null)");
				return ResponseEntity.internalServerError().build();
			}
			return ResponseEntity.ok().body(paso);
		} catch (Exception e) { 
			log.error("Error al crear el paso: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
		
	}

	private int validarDatosDTO(CrearPasoDTO crearPaso) {
		log.info("Entramos a validar el paso: " + crearPaso);
		if (crearPaso.getViaje_id() == null || crearPaso.getViaje_id().equals(0)) {
			log.error("No llega el id_viaje");
			return 1;
		}
		if (crearPaso.getNombre() == null || crearPaso.getNombre().equals(0)) {
			log.error("No llega el nombre del paso");
			return 1;
		}
		return 0;
	}
	private int validarDatosPasoDTO(EditarPasoDTO editarPaso) {
		log.info("Entramos a validar el paso: " + editarPaso);
		if (editarPaso.getViaje_id() == null || editarPaso.getViaje_id().equals(0)) {
			log.error("No llega el id_viaje");
			return 1;
		}
		if (editarPaso.getIdPaso() == null || editarPaso.getIdPaso().equals(0)) {
			log.error("No llega el getIdPaso");
			return 1;
		}
		if (editarPaso.getNombre() == null || editarPaso.getNombre().equals(0)) {
			log.error("No llega el nombre del paso");
			return 1;
		}
		return 0;
	}
}