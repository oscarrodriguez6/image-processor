package com.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imageProcessor.model.Usuario;
import com.imageProcessor.service.UsuarioService;
import com.webapp.service.EstadisticasService;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EstadisticasService estadisticasService;
	
	private static final Logger log = LoggerFactory.getLogger(EstadisticasController.class);

	@GetMapping("/estadisticasUsuario")
	public ResponseEntity<String> obtenerEstadisticasUsuario(@AuthenticationPrincipal UserDetails userDetails){
		
        Usuario usuario = null;
        
		try {
			usuario = usuarioService.validarUsuario(userDetails);
            log.info("Entramos a obtener las estadísticas de un usuario: " + usuario.getEmail());
		} catch (Exception e) {
			log.error("Error al validar usuario: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
        
		try {
			log.info("Entramos a obtener las estadísticas de un usuario: " + usuario.getEmail());
			String estadisticas = estadisticasService.obtenerEstadisticas(usuario.getId());
			return ResponseEntity.ok().body(estadisticas);
		} catch (Exception e) {
			log.error("Error al obtener estadísticas: " + e.getMessage());
			return ResponseEntity.internalServerError().build();
		}

	}

}
