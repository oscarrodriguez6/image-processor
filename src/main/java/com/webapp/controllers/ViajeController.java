package com.webapp.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.imageProcessor.dto.viajes.ContadorGeneralViajesDTO;
import com.imageProcessor.dto.viajes.CrearViajeDTO;
import com.imageProcessor.dto.viajes.ViajeDTO;
import com.imageProcessor.model.Usuario;
import com.imageProcessor.service.UsuarioService;
import com.webapp.service.ContadoresGeneralesService;
import com.webapp.service.ViajeService;

@RestController
public class ViajeController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired 
	private ViajeService viajeService;
	
	@Autowired
	private ContadoresGeneralesService contadoresGeneralesService;
	
	private static final Logger log = LoggerFactory.getLogger(ViajeController.class);

	@PostMapping("/crearViaje")
	public ResponseEntity<CrearViajeDTO> crearViaje (@AuthenticationPrincipal UserDetails userDetails, @RequestBody String viajeJson) {
		
        Usuario usuario = null;
        
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Entramos a crear un viaje : " + usuario.getEmail());
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
        
		try {
			CrearViajeDTO viaje =viajeService.crearViaje(usuario.getId(),viajeJson);
			if (viaje == null) {
				log.error("Error al crear el viaje: viaje no creado (null)");
				return ResponseEntity.internalServerError().build();
			}
			log.info("Viaje creado: " + viaje.getId() + " con nombre: " + viaje.getNombre());
			return ResponseEntity.ok().body(viaje);
		} catch (Exception e) { 
			log.error("Error al crear el viaje: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@DeleteMapping("/eliminarViaje/{id}")
	public ResponseEntity<String> eliminarViaje (@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
		
        Usuario usuario = null;
        
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Entramos a eliminar un viaje : " + usuario.getEmail());
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
        
		try {
			String viaje =viajeService.eliminarViaje(usuario.getId(),id);
			if (viaje == null || viaje.equals("KO")) {
				log.error("Error al eliminar el viaje");
				return ResponseEntity.internalServerError().build();
			}
			log.info("Viaje eliminado: " + id);
			return ResponseEntity.ok().body("Viaje eliminado");
		} catch (Exception e) { 
			log.error("Error al eliminar el viaje: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@PutMapping("/actualizarViaje")
	public ResponseEntity<CrearViajeDTO> actualizarViaje (@AuthenticationPrincipal UserDetails userDetails, @RequestBody String viajeJson) {
		
        Usuario usuario = null;
        
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Entramos a actualizar un viaje : " + usuario.getEmail());
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
        
		try {
			CrearViajeDTO viaje =viajeService.actualizarViaje(usuario.getId(),viajeJson);
			if (viaje == null) {
				log.error("Error al actualizar el viaje: viaje no actualizado (null)");
				return ResponseEntity.internalServerError().build();
			}
			log.info("Viaje actualizado");
			return ResponseEntity.ok().body(viaje);
		} catch (Exception e) { 
			log.error("Error al crear el viaje: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/obtenerViajesGenerales")
	public ResponseEntity<String> obtenerViajesGenerales(@AuthenticationPrincipal UserDetails userDetails){
		
        Usuario usuario = null;
        
		try {
			usuario = usuarioService.validarUsuario(userDetails);
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
        
		try {
			return ResponseEntity.ok().body(viajeService.consultarViajesGenerales(usuario.getId()));
		} catch (Exception e) {
			log.error("Error al buscar banderas: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/obtenerPaisesVisitados")
	public ResponseEntity<List<String>> obtenerPaisesVisitados(@AuthenticationPrincipal UserDetails userDetails){
        
        Usuario usuario = null;
        
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Entramos a buscar paises visitados para: " + usuario.getEmail());
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
        
		try {
			return ResponseEntity.ok().body(viajeService.obtenerPaises(usuario.getId()));
		} catch (Exception e) {
			log.error("Error al buscar banderas: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/obtenerContadoresGenerales")
	public ResponseEntity<String> obtenerContadoresGenerales(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = null;
        
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Entramos a obtener contadores generales para: " + usuario.getEmail());
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
        
		try {
			ContadorGeneralViajesDTO contadores = contadoresGeneralesService.buscarContadores(usuario.getId());
			ObjectMapper mapper = new ObjectMapper();
			String contadoresJson = mapper.writeValueAsString(contadores);
			log.info("Devolvemos el json: " + contadoresJson);
			return ResponseEntity.ok().body(contadoresJson);
		} catch (Exception e) {
			log.error("Error al obtener contadores generales: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/obtenerViaje/{id}")
	public ResponseEntity<String> obtenerViaje(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Usuario usuario = null;
        
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Entramos a obtener viaje generales para: " + usuario.getEmail());
            if (viajeService.validarUsuarioViaje(usuario.getId(),id) != 0) {
    			log.error("Error al validar usuario. No pertenece al viaje");
    			return ResponseEntity.internalServerError().build();
            }
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
        
		try {
			ViajeDTO viaje = viajeService.buscarViaje(id);
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			log.info("Antes de pasar el viaje a Json");
			String viajeJson = mapper.writeValueAsString(viaje);
			log.info("Viaje en Json: " + viajeJson);
			return ResponseEntity.ok().body(viajeJson);
		} catch (Exception e) {
			log.error("Error al obtener el viaje: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}
	
}
