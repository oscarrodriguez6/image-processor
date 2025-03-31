package com.imageProcessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imageProcessor.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre(String nombre);
}
