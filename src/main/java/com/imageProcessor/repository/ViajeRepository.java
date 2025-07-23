package com.imageProcessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.imageProcessor.model.Viaje;

public interface ViajeRepository extends JpaRepository<Viaje, Long>{
    
	@Query(value = "SELECT * FROM viaje WHERE usuario_id = :idUsuario", nativeQuery = true)
    List<Viaje> viajesPorUsuario(@Param("idUsuario") Long idUsuario);
   
	@Query(value = "SELECT * FROM viaje WHERE id = :id", nativeQuery = true)
    Viaje viajesPorId(@Param("id") Long id);
   
	@Query(value = "SELECT COUNT(DISTINCT(pais)) FROM viaje WHERE usuario_id = :idUsuario", nativeQuery = true)
    int paisesPorUsuario(@Param("idUsuario") Long idUsuario);

	@Query(value = "SELECT COUNT(DISTINCT(continente)) FROM viaje WHERE usuario_id = :idUsuario", nativeQuery = true)
    int continentesPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT DISTINCT(pais) FROM viaje WHERE usuario_id = :idUsuario", nativeQuery = true)
    List<String> listaPaisesPorUsuario(@Param("idUsuario") Long idUsuario);

	@Query(value = "SELECT COUNT(*) FROM viaje WHERE id = :idViaje AND usuario_id = :idUsuario", nativeQuery = true)
    int validaViajeUsuario(@Param("idViaje") Long idViaje,@Param("idUsuario") Long idUsuario);

}
