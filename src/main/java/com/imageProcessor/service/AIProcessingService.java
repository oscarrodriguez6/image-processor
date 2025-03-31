package com.imageProcessor.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.imageProcessor.controller.CargaImagenController;

import org.springframework.core.io.FileSystemResource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIProcessingService {

	private static final Logger log = LoggerFactory.getLogger(AIProcessingService.class);

	private final WebClient webClient;

	public AIProcessingService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
	}

/*	public List<String> procesarImagen(File imagen) {
		FileSystemResource recurso = new FileSystemResource(imagen);

	    Map<String, Object> respuesta = webClient.post()
	    		.uri("/procesar-imagen/")
	            .contentType(MediaType.MULTIPART_FORM_DATA)
	            .bodyValue(recurso)
	            .retrieve()
	            .bodyToMono(Map.class)
	            .block();

	    return (List<String>) respuesta.get("etiquetas");
	}
*/
	public List<String> procesarImagen(File imagen) {

		log.info("Entra a tratar la imagen: " + imagen);
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
	    bodyMap.add("file", new FileSystemResource(imagen));
	    
	    List<String> todasEtiquetas = new ArrayList<>();
	    
	    try {
		    Map<String, Object> respuesta = webClient.post()
		            .uri("/procesar-imagen/")
		            .contentType(MediaType.MULTIPART_FORM_DATA)
		            .body(BodyInserters.fromMultipartData(bodyMap))
		            .retrieve()
		            .bodyToMono(Map.class)
		            .block();
	
		    for (String key : respuesta.keySet()) {
		        if (key.startsWith("etiquetas_")) {
		            List<String> etiquetas = (List<String>) respuesta.get(key);
		            todasEtiquetas.addAll(etiquetas);
		        }
		    } 
				
		}catch (Exception e) {
			log.error("Error al procesar por IA la imagen: " + imagen);
			log.error(e.getMessage());
			todasEtiquetas=null;
		} 
	    log.info("Termina de tratar la imagen: " + imagen);
		return todasEtiquetas;
		
	}

}
