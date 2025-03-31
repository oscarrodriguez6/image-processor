package com.webapp.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imageProcessor.dto.ImageDTO;
import com.imageProcessor.model.Usuario;
import com.imageProcessor.service.ImageService;
import com.imageProcessor.service.UsuarioService;
import com.imageProcessor.utils.MetadataExtractor;

@RestController
@RequestMapping("/imagenes")
public class ImageController {
    
    @Autowired
    private UsuarioService usuarioService;

    private final ImageService imagenService;

    public ImageController(ImageService imagenService) {
        this.imagenService = imagenService;
    }

    @GetMapping("/miniatura/{id}")
    public ResponseEntity<Resource> obtenerMiniatura(@PathVariable Long id) {
        Resource miniatura = imagenService.obtenerMiniatura(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + miniatura.getFilename() + "\"")
                .body(miniatura);
    }

    @GetMapping("/original/{id}")
    public ResponseEntity<Resource> obtenerImagenOriginal(@PathVariable Long id) {
        Resource imagenOriginal = imagenService.obtenerImagenOriginal(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagenOriginal.getFilename() + "\"")
                .body(imagenOriginal);
    }
    
    @GetMapping("/miniaturas_ANT")
    public ResponseEntity<Page<ImageDTO>> obtenerMiniaturas(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "none") String query) {

    	System.out.println("Query: " + query);
        Usuario usuario = usuarioService.validarUsuario(userDetails);
        Pageable pageable = PageRequest.of(page, size);

        Page<ImageDTO> miniaturas = null;
        
        if (query.equals("none")) {
        	miniaturas = imagenService.obtenerMiniaturasPorUsuario(usuario.getId(), pageable);
        } else {
        	query = MetadataExtractor.tratarCaracteres(query);
        	miniaturas = imagenService.obtenerMiniaturasPorUsuarioClaves(usuario.getId(), query, pageable);
        }
        return ResponseEntity.ok(miniaturas);
    }

    @GetMapping("/miniaturas")
    public ResponseEntity<Page<ImageDTO>> obtenerMiniaturasFechas(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "none") String query,
            @RequestParam(defaultValue = "none") String searchTermDesde,
            @RequestParam(defaultValue = "none") String searchTermHasta) {

    	System.out.println("Query: " + query);
    	System.out.println("searchTermDesde: " + searchTermDesde);
    	System.out.println("searchTermHasta: " + searchTermHasta);
        Usuario usuario = usuarioService.validarUsuario(userDetails);
        Pageable pageable = PageRequest.of(page, size);

        Page<ImageDTO> miniaturas = null;
        
        if (query.equals("none") && searchTermDesde.equals("none") && searchTermHasta.equals("none")) {
        	miniaturas = imagenService.obtenerMiniaturasPorUsuario(usuario.getId(), pageable);
        } else if(!query.equals("none") && searchTermDesde.equals("none") && searchTermHasta.equals("none")) {
        	query = MetadataExtractor.tratarCaracteres(query);
        	miniaturas = imagenService.obtenerMiniaturasPorUsuarioClaves(usuario.getId(), query, pageable);
        } else if(query.equals("none") && !searchTermDesde.equals("none") && searchTermHasta.equals("none")) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	LocalDate fechaLocalDate = LocalDate.parse(searchTermDesde, formatter);
            LocalDateTime fechaDesde = fechaLocalDate.atStartOfDay();
        	miniaturas = imagenService.obtenerMiniaturasPorUsuarioDesde(usuario.getId(), fechaDesde, pageable);
        } else if(query.equals("none") && searchTermDesde.equals("none") && !searchTermHasta.equals("none")) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	LocalDate fechaLocalDate = LocalDate.parse(searchTermHasta, formatter);
            LocalDateTime fechaHasta = fechaLocalDate.atStartOfDay();
        	miniaturas = imagenService.obtenerMiniaturasPorUsuarioHasta(usuario.getId(), fechaHasta, pageable);
	    } else if(query.equals("none") && !searchTermDesde.equals("none") && !searchTermHasta.equals("none")) {
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    	LocalDate fechaLocalDate = LocalDate.parse(searchTermHasta, formatter);
	        LocalDateTime fechaHasta = fechaLocalDate.atStartOfDay();
	    	fechaLocalDate           = LocalDate.parse(searchTermDesde, formatter);
	        LocalDateTime fechaDesde = fechaLocalDate.atStartOfDay();
	    	miniaturas = imagenService.obtenerMiniaturasPorUsuarioDesdeHasta(usuario.getId(), fechaDesde, fechaHasta, pageable);
        } else if(!query.equals("none") && !searchTermDesde.equals("none") && searchTermHasta.equals("none")) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	LocalDate fechaLocalDate = LocalDate.parse(searchTermDesde, formatter);
            LocalDateTime fechaDesde = fechaLocalDate.atStartOfDay();
        	query = MetadataExtractor.tratarCaracteres(query);
        	miniaturas = imagenService.obtenerMiniaturasPorUsuarioClavesDesde(usuario.getId(), query, fechaDesde, pageable);
        } else if(!query.equals("none") && searchTermDesde.equals("none") && !searchTermHasta.equals("none")) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	LocalDate fechaLocalDate = LocalDate.parse(searchTermHasta, formatter);
            LocalDateTime fechaHasta = fechaLocalDate.atStartOfDay();
        	query = MetadataExtractor.tratarCaracteres(query);
        	miniaturas = imagenService.obtenerMiniaturasPorUsuarioClavesHasta(usuario.getId(), query, fechaHasta, pageable);
	    } else if(!query.equals("none") && !searchTermDesde.equals("none") && !searchTermHasta.equals("none")) {
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    	LocalDate fechaLocalDate = LocalDate.parse(searchTermHasta, formatter);
	        LocalDateTime fechaHasta = fechaLocalDate.atStartOfDay();
	    	fechaLocalDate           = LocalDate.parse(searchTermDesde, formatter);
	        LocalDateTime fechaDesde = fechaLocalDate.atStartOfDay();
        	query = MetadataExtractor.tratarCaracteres(query);
	    	miniaturas = imagenService.obtenerMiniaturasPorUsuarioClavesDesdeHasta(usuario.getId(), query, fechaDesde, fechaHasta, pageable);
	    }
        return ResponseEntity.ok(miniaturas);
    }

}
