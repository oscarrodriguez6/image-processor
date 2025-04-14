package com.imageProcessor.service;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.imageProcessor.controller.UsuarioController;
import com.imageProcessor.model.Usuario;
import com.imageProcessor.repository.UsuarioRepository;
import com.imageProcessor.security.JwtUtil;

@Service
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    public UsuarioService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public Usuario registrarUsuario(String nombre, String email, String password) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
        	log.error("Error usuario ya existe: " + email);
            throw new IllegalArgumentException("El email ya está en uso");
        }

        try {
        	Usuario usuario = new Usuario();
        	usuario.setNombre(nombre);
        	usuario.setEmail(email);
        	usuario.setPassword(passwordEncoder.encode(password));
        	return usuarioRepository.save(usuario);
        } catch (Exception e) {
        	log.error("Error interno del sistema: " + e.getMessage());
        	throw new IllegalArgumentException("Error al registrarse. Inténtelo de nuevo");
		}
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public String autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(password, usuarioOpt.get().getPassword())) {
            return jwtUtil.generarToken(email);
        }
        throw new IllegalArgumentException("Credenciales incorrectas");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new User(usuario.getEmail(), usuario.getPassword(), Collections.emptyList());
    }

    public Usuario validarUsuario(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        
        String emailUsuario = userDetails.getUsername();  // Obtener email del usuario autenticado
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(emailUsuario);
        
        if (usuarioOptional.isEmpty()) {
            return null;
        }
        
        return usuarioOptional.get();
   	
    }
    

    
}
