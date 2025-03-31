package com.imageProcessor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imageProcessor.service.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestParam String nombre, 
                                              @RequestParam String email, 
                                              @RequestParam String password) {
        try {
            usuarioService.registrarUsuario(nombre, email, password);
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            String token = usuarioService.autenticarUsuario(email, password);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
