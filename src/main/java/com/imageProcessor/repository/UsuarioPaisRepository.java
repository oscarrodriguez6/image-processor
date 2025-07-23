package com.imageProcessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.imageProcessor.model.UsuarioPais;

public interface UsuarioPaisRepository extends JpaRepository<UsuarioPais, Long>{
    
	@Query(value = "SELECT COUNT(DISTINCT(pais)) FROM usuarioPais WHERE usuario_id = :idUsuario", nativeQuery = true)
    int paisesPorUsuario(@Param("idUsuario") Long idUsuario);

	@Query(value = "SELECT COUNT(DISTINCT(continente)) FROM usuarioPais WHERE usuario_id = :idUsuario", nativeQuery = true)
    int continentesPorUsuario(@Param("idUsuario") Long idUsuario);

}
