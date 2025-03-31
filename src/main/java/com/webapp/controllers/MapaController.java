package com.webapp.controllers;

import com.imageProcessor.dto.ClusterDTO;
import com.imageProcessor.dto.ImagenMapaDTO;
import com.imageProcessor.model.Image;
import com.imageProcessor.model.Usuario;
import com.imageProcessor.service.ImageService;
import com.imageProcessor.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MapaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ImageService imageService;

    public MapaController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/verMapa")
    public String mostrarMapa() {
        return "mapa"; 
    }
    @GetMapping("/coordenadas")
    public ResponseEntity<List<ImagenMapaDTO>> obtenerImagenesPorCoordenadas(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("minLat") Double minLat,
            @RequestParam("maxLat") Double maxLat,
            @RequestParam("minLon") Double minLon,
            @RequestParam("maxLon") Double maxLon) {
        
        Usuario usuario = usuarioService.validarUsuario(userDetails);

        List<Image> imagenes = imageService.obtenerImagenesConCoordenadas(usuario.getId(), minLat, maxLat, minLon, maxLon);
        
        List<ImagenMapaDTO> imagenesDTO = imagenes.stream().map(img -> new ImagenMapaDTO(
                img.getId(),
                img.getCoordenadas().getX(),  // Longitud
                img.getCoordenadas().getY(),  // Latitud
                img.getRuta(),
                img.getThumbnailUrl()
        )).collect(Collectors.toList());
        
        return ResponseEntity.ok(imagenesDTO);
    }
    
    @GetMapping("/coordenadas2")
    public ResponseEntity<?> obtenerImagenesOClusters(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("minLat") Double minLat,
            @RequestParam("maxLat") Double maxLat,
            @RequestParam("minLon") Double minLon,
            @RequestParam("maxLon") Double maxLon) {
        
        Usuario usuario = usuarioService.validarUsuario(userDetails);
                
        int totalImagenes = imageService.contarImagenes(usuario.getId(), minLat, maxLat, minLon, maxLon);
        
        if (totalImagenes > 100) {
            List<ClusterDTO> clusters = imageService.obtenerClusters(usuario.getId(), minLat, maxLat, minLon, maxLon);
            return ResponseEntity.ok(clusters);
        } else {
        	List<Image> imagenes = imageService.obtenerImagenesConCoordenadas(usuario.getId(), minLat, maxLat, minLon, maxLon);
            
            List<ImagenMapaDTO> imagenesDTO = imagenes.stream().map(img -> new ImagenMapaDTO(
                    img.getId(),
                    img.getCoordenadas().getX(),  // Longitud
                    img.getCoordenadas().getY(),  // Latitud
                    img.getRuta(),
                    img.getThumbnailUrl()
            )).collect(Collectors.toList());
            return ResponseEntity.ok(imagenesDTO);
        }
    }

}
