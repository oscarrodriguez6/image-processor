package com.imageProcessor.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.imageProcessor.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	boolean existsByRuta(String ruta);
	
	Image findByRuta(String ruta);
    
	List<Image> findByUsuarioId(Long usuarioId);

	// Por usuario
//    @Query("SELECT i FROM Image i WHERE i.usuarioId = :idUsuario ORDER BY i.fecha DESC")
//    Page<Image> findByUsuarioId(@Param("idUsuario") Long idUsuario, Pageable pageable);
    
	// Por usuario
	@Query("SELECT i FROM Image i WHERE i.usuario.id = :idUsuario ORDER BY i.fecha DESC")
	Page<Image> findByUsuarioId(@Param("idUsuario") Long idUsuario, Pageable pageable);
    
    // Por usuario y clave
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClave(@Param("idUsuario") Long idUsuario, @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClave(@Param("idUsuario") Long idUsuario, @Param("query1") String query1, @Param("query2") String query2, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClave(@Param("idUsuario") Long idUsuario, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query4) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClave(@Param("idUsuario") Long idUsuario, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query4) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query5) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClave(@Param("idUsuario") Long idUsuario, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, @Param("query5") String query5, Pageable pageable);

    // Por usuario y clave parcial
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query || '%') ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveParcial(@Param("idUsuario") Long idUsuario, @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%')) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveParcial(@Param("idUsuario") Long idUsuario, @Param("query1") String query1, @Param("query2") String query2, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%')) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveParcial(@Param("idUsuario") Long idUsuario, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query4 || '%')) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveParcial(@Param("idUsuario") Long idUsuario, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query4 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query5 || '%')) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveParcial(@Param("idUsuario") Long idUsuario, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, @Param("query5") String query5, Pageable pageable);

    // Por fecha desde
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdFechaDesde(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, Pageable pageable);

    // Por fecha desde y claves
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesde(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesde(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query1") String query1, @Param("query2") String query2, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesde(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query4) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesde(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query4) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query5) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesde(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, @Param("query5") String query5, Pageable pageable);

    // Por fecha desde y clave parcial
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query || '%') ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND EXISTS ((SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%')) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query1") String query1, @Param("query2") String query2, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%')) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query4 || '%')) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query4 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query5 || '%')) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, @Param("query5") String query5, Pageable pageable);

    // Por fecha hasta
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdFechaHasta(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, Pageable pageable);

    // Por fecha hasta y claves
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHasta(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHasta(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query1") String query1, @Param("query2") String query2, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHasta(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query4) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHasta(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query4) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query5) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHasta(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, @Param("query5") String query5, Pageable pageable);

    // Por fecha hasta y clave parcial
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query || '%') ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHastaParcial(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND EXISTS ((SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%')) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHastaParcial(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query1") String query1, @Param("query2") String query2, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%')) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHastaParcial(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query4 || '%')) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHastaParcial(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha <= :hasta AND (EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query4 || '%') OR EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query5 || '%')) ORDER BY fecha", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveHastaParcial(@Param("idUsuario") Long idUsuario, @Param("hasta") LocalDateTime hasta, @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, @Param("query5") String query5, Pageable pageable);

    // Por fecha desde y hasta
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdFechaDesdeHasta(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta, Pageable pageable);

    // Por fecha desde, hasta y claves
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHasta(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHasta(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query1") String query1, @Param("query2") String query2, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHasta(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query4) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHasta(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query1) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query2) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query3) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query4) AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value = :query5) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHasta(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, @Param("query5") String query5, Pageable pageable);

    // Por fecha desde, hasta y clave parcial
    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query || '%') ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHastaParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS ((SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%')) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHastaParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query1") String query1, @Param("query2") String query2, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS ((SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%')) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHastaParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS ((SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query4 || '%')) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHastaParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND fecha >= :desde AND fecha <= :hasta AND EXISTS ((SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query1 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query2 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query3 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query4 || '%') AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE value LIKE '%' || :query5 || '%')) ORDER BY fecha DESC", nativeQuery = true)
    Page<Image> findByUsuarioIdClaveDesdeHastaParcial(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta,  @Param("query1") String query1, @Param("query2") String query2, @Param("query3") String query3, @Param("query4") String query4, @Param("query5") String query5, Pageable pageable);

    @Query(value = "SELECT * FROM image WHERE usuario_id = :idUsuario AND ST_X(coordenadas) BETWEEN :lonMin AND :lonMax AND ST_Y(coordenadas) BETWEEN :latMin AND :latMax", nativeQuery = true)
    List<Image> findByUsuarioIdAndCoordenadasBetween(@Param("idUsuario") Long idUsuario,
                                                      @Param("latMin") Double latMin,
                                                      @Param("latMax") Double latMax,
                                                      @Param("lonMin") Double lonMin,
                                                      @Param("lonMax") Double lonMax);

/*    @Query("SELECT COUNT(i) FROM Image i WHERE i.usuario.id = :idUsuario AND i.ST_X(coordenadas) BETWEEN :minLon AND :maxLon AND i.ST_Y(coordenadas) BETWEEN :minLat AND :maxLat")
    int contarImagenesEnArea(@Param("idUsuario") Long idUsuario, @Param("minLat") double minLat, @Param("maxLat") double maxLat,
                              @Param("minLon") double minLon, @Param("maxLon") double maxLon);
*/
    @Query(value = "SELECT COUNT(*) FROM image WHERE usuario_id = :idUsuario AND ST_X(coordenadas) BETWEEN :minLon AND :maxLon AND ST_Y(coordenadas) BETWEEN :minLat AND :maxLat", nativeQuery = true)
    int contarImagenesEnArea(@Param("idUsuario") Long idUsuario, @Param("minLat") double minLat, @Param("maxLat") double maxLat,
                              @Param("minLon") double minLon, @Param("maxLon") double maxLon);

    @Query(value = "SELECT ST_X(ST_Centroid(ST_Collect(coordenadas))) AS longitud, ST_Y(ST_Centroid(ST_Collect(coordenadas))) AS latitud, COUNT(*) AS cantidad, 100 AS radio FROM image WHERE usuario_id = :idUsuario AND ST_IsEmpty(coordenadas) = FALSE AND ST_X(coordenadas) BETWEEN :lonMin AND :lonMax AND ST_Y(coordenadas) BETWEEN :latMin AND :latMax GROUP BY ST_SnapToGrid(coordenadas, 0.001, 0.001)", nativeQuery = true)
    	List<Object[]> obtenerClusters(@Param("idUsuario") Long idUsuario, 
     	                               @Param("latMin") Double latMin, @Param("latMax") Double latMax,
     	                               @Param("lonMin") Double lonMin, @Param("lonMax") Double lonMax);


}


