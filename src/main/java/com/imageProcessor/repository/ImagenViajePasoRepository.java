package com.imageProcessor.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.imageProcessor.model.Imagen_viaje_paso;

public interface ImagenViajePasoRepository extends JpaRepository<Imagen_viaje_paso, Long> {

	@Modifying
	@Query(value = "DELETE FROM imagen_viaje_paso WHERE viaje_id = :idViaje AND paso_id = :idPaso", nativeQuery = true)
    void borrarImagenesPorPaso(@Param("idViaje") Long idViaje, @Param("idPaso") Long idPaso);

	@Query(value = "SELECT COUNT(*) FROM imagen_viaje_paso WHERE viaje_id = :idViaje AND paso_id = :idPaso", nativeQuery = true)
    int contarImagenesPorPaso(@Param("idViaje") Long idViaje, @Param("idPaso") Long idPaso);

	@Query(value = "SELECT * FROM imagen_viaje_paso WHERE viaje_id = :idViaje AND paso_id = :idPaso", nativeQuery = true)
    List<Imagen_viaje_paso> buscarImagenesPorPaso(@Param("idViaje") Long idViaje, @Param("idPaso") Long idPaso);

	@Modifying
	@Query(value = "DELETE FROM imagen_viaje_paso WHERE viaje_id = :idViaje", nativeQuery = true)
    void borrarImagenesPorViaje(@Param("idViaje") Long idViaje);

}
