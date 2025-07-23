package com.imageProcessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.imageProcessor.model.Paso;

public interface PasoRepository extends JpaRepository<Paso, Long>{

	@Query(value = "SELECT * FROM paso WHERE viaje_id = :idViaje ORDER BY fecha", nativeQuery = true)
    List<Paso> pasosPorViaje(@Param("idViaje") Long idViaje);

	@Query(value = "SELECT COUNT(*) FROM paso WHERE viaje_id = :idViaje", nativeQuery = true)
    int countPasosPorViaje(@Param("idViaje") Long idViaje);

	@Modifying
	@Query(value = "DELETE FROM paso WHERE viaje_id = :idViaje", nativeQuery = true)
    void borrarPasosPorViaje(@Param("idViaje") Long idViaje);

}
