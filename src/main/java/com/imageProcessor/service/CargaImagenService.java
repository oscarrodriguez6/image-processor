package com.imageProcessor.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.imageProcessor.model.Image;
import com.imageProcessor.model.Usuario;
import com.imageProcessor.repository.ImageRepository;
import com.imageProcessor.utils.MetadataExtractor;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class CargaImagenService {

	private static final Logger log = LoggerFactory.getLogger(CargaImagenService.class);
	private static final String UPLOAD_DIR = "uploads/miniaturas/";
    private static final int BATCH_SIZE = 200;
	
    @Autowired
    private ImageService imagenService;

    @Autowired
    private ImageRepository imagenRepository;


//    @Autowired
//    private AIProcessingService imageProcessingService;
    
    @Value("${imagenes.carpeta}")
    private String carpetaImagenes;
    
    public HashMap<String, Integer> procesarCarpetaFotos (String carpeta, Usuario usuario, int opcionTratamiento) {
    	
        List<File> imagenes = imagenService.obtenerNuevasImagenes(carpeta, opcionTratamiento);
        List<Image> batch = new ArrayList<>();                                                    // Acumulador para guardar en lotes


    	int imagenesProcesadas = 0;
    	int imagenesIgnoradas = 0;
    	int imagenesError = 0;
    	
        for (File imagen : imagenes) {
        	
        	log.info("Imagen a cargar: " + imagen.getAbsolutePath());
        	
        	Image imagenExistente = null;
        	imagenExistente = imagenRepository.findByRuta(imagen.getAbsolutePath());
        	
        	if (imagenExistente == null) {
        		Image imagenNueva = procesarImagen(imagen, usuario);
        		if (imagenNueva != null) {
        			batch.add(imagenNueva);
        			imagenesProcesadas++;
        		} else {
        			imagenesError++;
        		}

        	} else {
                switch (opcionTratamiento) {
                case 0:                                                  // No tratar si ya está en la BD
                	imagenesIgnoradas++;
                    break;
                    
                case 1:                                                  // Reprocesar completamente
                	Image imagenActualizada = actualizarImagen(imagen, imagenExistente, usuario);
                    if (imagenActualizada != null) {
                    	batch.add(imagenActualizada);
                    	imagenesProcesadas++;
                    } else {
                    	imagenesError++;
                    }
                    break;
                    
                case 2:                                                  // Solo actualizar etiquetas IA (para un futuro)
                    if (actualizarImagenIA(imagen, imagenExistente, usuario)) {
                    	imagenesProcesadas++;
                    }else {
                    	imagenesError++;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Opción de tratamiento no válida.");
                }
        		
        	}
            // Guardar en la BD cada 200 imágenes procesadas
            if (batch.size() >= BATCH_SIZE) {
                guardarBatch(batch);
                batch.clear();
            }

        }
        
        guardarBatch(batch);
        batch.clear();
        
        HashMap<String, Integer> contadores = new HashMap<>();
        contadores.put("imagenesProcesadas", imagenesProcesadas);
        contadores.put("imagenesIgnoradas", imagenesIgnoradas);
        contadores.put("imagenesError", imagenesError);
        return contadores;
    }

    public HashMap<String, Integer> procesarCarpetaFotos (MultipartFile[] files, Usuario usuario, int opcionTratamiento) {
    	
        List<Image> batch = new ArrayList<>();                                                    // Acumulador para guardar en lotes


    	int imagenesProcesadas = 0;
    	int imagenesIgnoradas = 0;
    	int imagenesError = 0;
    	
    	for (MultipartFile file : files) {
            try {

            	log.info("Imagen a cargar: " + file.getName());
            	
            	String relativePath = file.getOriginalFilename();
                File imagen = new File(carpetaImagenes + "/" + usuario.getUsername() + "/" + relativePath);
                
                // Crea las carpetas si no existen
                if (!imagen.getParentFile().exists()) {
                	imagen.getParentFile().mkdirs();
                }
                
                // Guarda la imagen en el servidor
                file.transferTo(imagen);
                
            	Image imagenExistente = imagenRepository.findByRuta(imagen.getAbsolutePath());

	        	if (imagenExistente == null) {
	        		Image imagenNueva = procesarImagen(imagen, usuario);
	        		if (imagenNueva != null) {
	        			batch.add(imagenNueva);
	        			imagenesProcesadas++;
	        		} else {
	        			imagenesError++;
	        		}
	
	        	} else {
	                switch (opcionTratamiento) {
	                case 0:                                                  // No tratar si ya está en la BD
	                	imagenesIgnoradas++;
	                    break;
	                    
	                case 1:                                                  // Reprocesar completamente
	                	Image imagenActualizada = actualizarImagen(imagen, imagenExistente, usuario);
	                    if (imagenActualizada != null) {
	                    	batch.add(imagenActualizada);
	                    	imagenesProcesadas++;
	                    } else {
	                    	imagenesError++;
	                    }
	                    break;
	                    
	                case 2:                                                  // Solo actualizar etiquetas IA (para un futuro)
	                    if (actualizarImagenIA(imagen, imagenExistente, usuario)) {
	                    	imagenesProcesadas++;
	                    }else {
	                    	imagenesError++;
	                    }
	                    break;
	                default:
	                    throw new IllegalArgumentException("Opción de tratamiento no válida.");
	                }
	            }

	        	// Guardar en la BD cada 200 imágenes procesadas
	            if (batch.size() >= BATCH_SIZE) {
	                guardarBatch(batch);
	                batch.clear();
	            }
            } catch (IOException e) {
                // Manejo de errores
                imagenesError++;
                e.printStackTrace();	        		
        	}
        }
        
        guardarBatch(batch);
        batch.clear();
        
        HashMap<String, Integer> contadores = new HashMap<>();
        contadores.put("imagenesProcesadas", imagenesProcesadas);
        contadores.put("imagenesIgnoradas", imagenesIgnoradas);
        contadores.put("imagenesError", imagenesError);
        return contadores;
    }

    private Image procesarImagen (File imagen, Usuario usuario) {
    	
    	try {
	    	Image nuevaImagen = new Image();
	    	
	    	// Obtener metadatos
	    	Map<String, Object> metadata = procesarMetadatos(imagen);
	             
	    	// Obtener descripción IA
	/*    	Map<String, Object> metadataIA = null; 
	    	metadataIA = procesarMetadatosIA(imagen);
	        nuevaImagen.setEtiquetas(metadataIA);
	        nuevaImagen.setProcesada(true);             // Aún no ha pasado por la IA
	*/
	    	String nombreArchivo = imagen.getName();
	        String rutaMiniatura = generarMiniatura(imagen.getAbsolutePath(), nombreArchivo, imagen);

        	Date fecha = (Date) metadata.get("Fecha");
        	LocalDateTime fechaBBDD = null;
	        if (fecha != null) {
	        	fechaBBDD = fecha.toInstant()
                        .atZone(ZoneId.systemDefault())       // Convierte a la zona horaria del sistema
                        .toLocalDateTime();                   // Obtiene LocalDateTime
	        }

	        if (metadata.get("Coordenadas") != null && !metadata.get("Coordenadas").equals("")) {
	            String[] coordsArray = ((String) metadata.get("Coordenadas")).split(" ");
	            double latitude = Double.parseDouble(coordsArray[0]);  // Primero la latitud (Y)
	            double longitude = Double.parseDouble(coordsArray[1]); // Luego la longitud (X)
	            
	            nuevaImagen.setCoordenadas(new GeometryFactory().createPoint(new Coordinate(longitude, latitude)));
	            
	            metadata.remove("Coordenadas");
	        }
	        // Guardar en BD

	        nuevaImagen.setFecha(fechaBBDD);
	        nuevaImagen.setThumbnailUrl(rutaMiniatura);
	        nuevaImagen.setRuta(imagen.getAbsolutePath());
	        nuevaImagen.setMetadata(metadata);
	        nuevaImagen.setUsuario(usuario);  // Asociamos la imagen al usuario
	
	        log.info("Imagen cargada");
	        
	        return nuevaImagen;
    	} catch (Exception e) {
			log.error("Error al procesar la imagen: " + imagen.getAbsolutePath());
			return null;
		}
    }
	
    private Image actualizarImagen(File imagen, Image imagenExistente, Usuario usuario){
    	
    	try {
	    	// Obtener metadatos
	    	Map<String, Object> metadata = procesarMetadatos(imagen);
	             
	    	// Obtener descripción IA
	/*    	Map<String, Object> metadataIA = null; 
	    	metadataIA = procesarMetadatosIA(imagen);
	    	imagenExistente.setEtiquetas(metadataIA);
	        imagenExistente.setProcesada(true);             // Aún no ha pasado por la IA
	*/
	    	String nombreArchivo = imagen.getName();
	        String rutaMiniatura = generarMiniatura(imagen.getAbsolutePath(), nombreArchivo, imagen);

	        // Guardar en BD

        	Date fecha = (Date) metadata.get("Fecha");
        	LocalDateTime fechaBBDD = null;
	        if (fecha != null) {
	        	fechaBBDD = fecha.toInstant()
                        .atZone(ZoneId.systemDefault())       // Convierte a la zona horaria del sistema
                        .toLocalDateTime();                   // Obtiene LocalDateTime
	        }
	        if (metadata.get("Coordenadas") != null && !metadata.get("Coordenadas").equals("")) {
	            String[] coordsArray = ((String) metadata.get("Coordenadas")).split(" ");
	            double latitude = Double.parseDouble(coordsArray[0]);  // Primero la latitud (Y)
	            double longitude = Double.parseDouble(coordsArray[1]); // Luego la longitud (X)
	            
	            imagenExistente.setCoordenadas(new GeometryFactory().createPoint(new Coordinate(longitude, latitude)));
	            
	            metadata.remove("Coordenadas");
	        }
	        
	        imagenExistente.setFecha(fechaBBDD);
	        imagenExistente.setThumbnailUrl(rutaMiniatura);
	        imagenExistente.setMetadata(metadata);
	        imagenExistente.setUsuario(usuario);  // Asociamos la imagen al usuario
	
	        log.info("Imagen actualizada");
	        return imagenExistente;
    	} catch (Exception e) {
			log.error("Error al actualizar la imagen: " + imagen.getAbsolutePath());
			return null;
		}
    	
    }
	
    private boolean actualizarImagenIA(File imagen, Image imagenExistente, Usuario usuario){
      
    	try {
	    	// Obtener descripción IA
	/*    	Map<String, Object> metadataIA = null; 
	    	metadataIA = procesarMetadatosIA(imagen);
	    	imagenExistente.setEtiquetas(metadataIA);
	        imagenExistente.setProcesada(true);             // Aún no ha pasado por la IA
	*/
	    	// Guardar en BD
	        imagenRepository.save(imagenExistente);
	            
	        log.info("Imagen actualizada");
	        return true;
    	} catch (Exception e) {
			log.error("Error al actualizar la IA de la imagen: " + imagen.getAbsolutePath());
			return false;
		}
    }
	
    // Extraer metadatos
	private Map<String, Object> procesarMetadatos(File imagen) {
        return MetadataExtractor.obtenerMetadatos(imagen);
	}

    // Extraer metadatos IA
/*	private Map<String, Object> procesarMetadatosIA(File imagen) {

		// Obtener etiquetas de la IA
        List<String> etiquetas = imageProcessingService.procesarImagen(imagen);
        
        Map<String, Object> metadata2 = new HashMap<>();
        if (etiquetas != null) {
        	for (int i = 0; i < etiquetas.size(); i++) {
        		metadata2.put("Etiqueta" + (i + 1),etiquetas.get(i)); 
        	}
        }
        return metadata2;
	}
*/
	
	private String generarMiniatura(String rutaOriginal, String nombreArchivo, File imagen) throws IOException {
	    
	    String carpetaMiniaturas = Paths.get(UPLOAD_DIR).toString();
	    File directorioMiniaturas = new File(carpetaMiniaturas);
	    
	    if (!directorioMiniaturas.exists()) {
	        directorioMiniaturas.mkdirs(); // Crear directorio si no existe
	    }

	    String rutaMiniatura = Paths.get(carpetaMiniaturas, "thumb_" + nombreArchivo).toString();

	    Thumbnails.of(imagen)
	              .size(720, 720)      // Tamaño de la miniatura
	              .outputQuality(0.7)  // Reducir calidad para optimizar
	              .toFile(new File(rutaMiniatura));

	    return "/uploads/miniaturas/thumb_" + nombreArchivo; // URL accesible desde el frontend
	}

    @Transactional
    public void guardarBatch(List<Image> batch) {
        imagenRepository.saveAll(batch);
        log.info("Guardado batch de {} imágenes", batch.size());
    }
}
