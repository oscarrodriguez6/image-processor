package com.imageProcessor.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imageProcessor.dto.CargaMasivaResponse;
import com.imageProcessor.model.Usuario;
import com.imageProcessor.service.CargaImagenService;
import com.imageProcessor.service.UsuarioService;

@RestController
@RequestMapping("/imagenes")
public class CargaImagenController {

	private static final Logger log = LoggerFactory.getLogger(CargaImagenController.class);

	@Autowired
    private CargaImagenService cargaImagenService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/carga-masiva-postman")
    public ResponseEntity<CargaMasivaResponse> cargaMasiva1(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam(required = false) String carpeta,
                                              @RequestParam(defaultValue = "1") int opcionTratamiento) {
    	
        log.info("Inicio proceso de carga");
        HashMap<String, Integer> contadores = new HashMap<>();
    	Usuario usuario = usuarioService.validarUsuario(userDetails);
    	if (usuario == null) {
            CargaMasivaResponse errorResponse = new CargaMasivaResponse(
                    userDetails.getUsername(),
                    carpeta != null ? carpeta : "Por defecto",
                    opcionTratamiento,
                    0,
                    0,
                    0,
                    "Error en la carga",
                    "");
            log.info("Usuario no autenticado");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    	}
        log.info("Carga masiva iniciada desde Postman para el usuario con usuario: ", usuario.getUsername());

        long inicio = System.nanoTime(); // También puedes usar System.currentTimeMillis();
        LocalDateTime horaIni = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String horaIniFormateada = horaIni.format(formato);        
 
        try {
        	contadores = cargaImagenService.procesarCarpetaFotos(carpeta, usuario, opcionTratamiento);
        } catch (Exception e) {
            CargaMasivaResponse errorResponse = new CargaMasivaResponse(
                    userDetails.getUsername(),
                    carpeta != null ? carpeta : "Por defecto",
                    opcionTratamiento,
                    contadores.get("imagenesProcesadas"),
                    contadores.get("imagenesIgnoradas"),
                    contadores.get("imagenesError"),
                    "Error en la carga",
                    "");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}

        log.info("Fin del proceso de carga");
        long fin = System.nanoTime(); // También puedes usar System.currentTimeMillis();
        long duracion = fin - inicio;
        LocalDateTime horaFin = LocalDateTime.now();
        String horaFinFormateada = horaFin.format(formato);        

        CargaMasivaResponse response = new CargaMasivaResponse(
                userDetails.getUsername(),
                carpeta != null ? carpeta : "Por defecto",
                opcionTratamiento,
                contadores.get("imagenesProcesadas"),
                contadores.get("imagenesIgnoradas"),
                contadores.get("imagenesError"),
                "Carga masiva iniciada correctamente. Hora de incio: " + horaIniFormateada + ". Termina " + horaFinFormateada,
                duracion);

        return ResponseEntity.ok(response);
    }
    	
	@PostMapping("/carga-masiva-web")
    public ResponseEntity<CargaMasivaResponse> cargaMasiva(@AuthenticationPrincipal UserDetails userDetails,
   	                                           @RequestParam("files") MultipartFile[] files,
   	                                           @RequestParam(defaultValue = "1") int opcionTratamiento) {
    	  
        log.info("Inicio proceso de carga");
        HashMap<String, Integer> contadores = new HashMap<>();
    	Usuario usuario = usuarioService.validarUsuario(userDetails);
    	if (usuario == null) {
            CargaMasivaResponse errorResponse = new CargaMasivaResponse(
                    userDetails.getUsername(),
                    "Por defecto",
                    opcionTratamiento,
                    0,
                    0,
                    0,
                    "Error en la carga",
                    "");
            log.info("Usuario no autenticado");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    	}
        log.info("Carga masiva iniciada desde la web para el usuario con usuario: ", usuario.getUsername());


        long inicio = System.nanoTime(); // También puedes usar System.currentTimeMillis();
        LocalDateTime horaIni = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String horaIniFormateada = horaIni.format(formato);        
 
        try {
        	contadores = cargaImagenService.procesarCarpetaFotos(files, usuario, opcionTratamiento);
        } catch (Exception e) {
            CargaMasivaResponse errorResponse = new CargaMasivaResponse(
                    userDetails.getUsername(),
                    "Por defecto",
                    opcionTratamiento,
                    contadores.get("imagenesProcesadas"),
                    contadores.get("imagenesIgnoradas"),
                    contadores.get("imagenesError"),
                    "Error en la carga",
                    "");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}

        log.info("Fin del proceso de carga");
        long fin = System.nanoTime(); // También puedes usar System.currentTimeMillis();
        long duracion = fin - inicio;
        LocalDateTime horaFin = LocalDateTime.now();
        String horaFinFormateada = horaFin.format(formato);        

        CargaMasivaResponse response = new CargaMasivaResponse(
                userDetails.getUsername(),
                "Por defecto",
                opcionTratamiento,
                contadores.get("imagenesProcesadas"),
                contadores.get("imagenesIgnoradas"),
                contadores.get("imagenesError"),
                "Carga masiva iniciada correctamente. Hora de incio: " + horaIniFormateada + ". Termina " + horaFinFormateada,
                duracion);

        return ResponseEntity.ok(response);
    }

}
