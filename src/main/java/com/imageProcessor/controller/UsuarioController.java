package com.imageProcessor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imageProcessor.service.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestParam String nombre, 
                                              @RequestParam String email, 
                                              @RequestParam String password) {
        try {
        	log.info("Se intenta registrar el usuario: " + nombre + ", " + email);
            usuarioService.registrarUsuario(nombre, email, password);
            log.info("Usuario registado con éxito");
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (IllegalArgumentException e) {
        	log.error("Error en el alta de un usuario: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
        	log.debug("Se intenta el ligin del usuario: " + email);
            String token = usuarioService.autenticarUsuario(email, password);
            log.debug("Usuario correcto");
            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException e) {
        	log.error("Error en el login del usuario: " + email);
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
