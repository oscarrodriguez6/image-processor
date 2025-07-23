package com.imageProcessor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.imageProcessor.dto.ClusterDTO;
import com.imageProcessor.dto.ImageDTO;
import com.imageProcessor.model.Image;
import com.imageProcessor.repository.ImageRepository;
import com.webapp.controllers.ImageController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ImageRepository imagenRepository;

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    public List<File> obtenerNuevasImagenes(String rutaCarpeta, int opcionTratamiento) {
        File carpeta = new File(rutaCarpeta);
        if ((!carpeta.exists() || !carpeta.isDirectory()) && opcionTratamiento == 1) {
            throw new RuntimeException("La carpeta de imágenes no existe o no es válida.");
        }

        // Lista para almacenar todas las imágenes encontradas
        List<File> imagenes = new ArrayList<>();
        buscarImagenesRecursivo(carpeta, imagenes);

        return imagenes;
    }

    private void buscarImagenesRecursivo(File carpeta, List<File> imagenes) {
        File[] archivos = carpeta.listFiles();
        if (archivos == null) return;

        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                // Si es una carpeta, buscar imágenes dentro de ella (recursividad)
                buscarImagenesRecursivo(archivo, imagenes);
            } else if (esImagen(archivo)) {
                // Si es un archivo de imagen, añadirlo a la lista
                imagenes.add(archivo);
            }
        }
    }

    private boolean esImagen(File file) {
        String nombre = file.getName().toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".png") || nombre.endsWith(".jpeg");
    }
    
    public Page<ImageDTO> obtenerMiniaturasPorUsuario(Long idUsuario, Pageable pageable) {
    	
    	EntityType<?> imageEntity = entityManager.getMetamodel().entity(Image.class);

        Page<Image> imagenes = imagenRepository.findByUsuarioId(idUsuario, pageable);
		return formatearSalida(imagenes);
    }

    public Resource obtenerImagenOriginal(Long id) {
        return obtenerImagen(id, false);
    }

    public Resource obtenerMiniatura(Long id) {
        return obtenerImagen(id, true);
    }

    private Resource obtenerImagen(Long id, boolean esMiniatura) {
        Optional<Image> imagenOptional = imagenRepository.findById(id);

        if (imagenOptional.isEmpty()) {
            throw new RuntimeException("Imagen no encontrada");
        }

        String rutaImagen = esMiniatura ? imagenOptional.get().getThumbnailUrl() 
                                        : imagenOptional.get().getRuta();

        try {
            Path path = Paths.get(rutaImagen);
            Resource recurso = new UrlResource(path.toUri());

            if (recurso.exists() || recurso.isReadable()) {
                return recurso;
            } else {
                throw new RuntimeException("No se pudo leer la imagen: " + rutaImagen);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la imagen", e);
        }
    }

	public Page<ImageDTO> obtenerMiniaturasPorUsuarioClaves(Long idUsuario, String query, String busquedaParcial, Pageable pageable) {
		String[] listaQuery = query.split(";");
		Page<Image> imagenes = null;
		
		if (busquedaParcial.equals("parcial")) {
			switch (listaQuery.length) {
				case 1:
				    log.info("SELECT * FROM image WHERE usuario_id = " + idUsuario + " AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE AND value like % " + listaQuery[0] + "%) ORDER BY fecha DESC");
					imagenes = imagenRepository.findByUsuarioIdClaveParcial(idUsuario, listaQuery[0], pageable);
					break;
				case 2:
					imagenes = imagenRepository.findByUsuarioIdClaveParcial(idUsuario, listaQuery[0], listaQuery[1], pageable);		
					break;
				case 3:
					imagenes = imagenRepository.findByUsuarioIdClaveParcial(idUsuario, listaQuery[0], listaQuery[1], listaQuery[2], pageable);		
					break;
				case 4:
					imagenes = imagenRepository.findByUsuarioIdClaveParcial(idUsuario, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], pageable);		
					break;
				case 5:
					imagenes = imagenRepository.findByUsuarioIdClaveParcial(idUsuario, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], listaQuery[4], pageable);		
					break;
				default:
					break;
			}
		} else {
			switch (listaQuery.length) {
				case 1:
				    log.info("SELECT * FROM image WHERE usuario_id = " + idUsuario + " AND EXISTS (SELECT 1 FROM jsonb_each_text(metadata) WHERE AND value = " + listaQuery[0] + ") ORDER BY fecha DESC");
					imagenes = imagenRepository.findByUsuarioIdClave(idUsuario, listaQuery[0], pageable);
					break;
				case 2:
					imagenes = imagenRepository.findByUsuarioIdClave(idUsuario, listaQuery[0], listaQuery[1], pageable);		
					break;
				case 3:
					imagenes = imagenRepository.findByUsuarioIdClave(idUsuario, listaQuery[0], listaQuery[1], listaQuery[2], pageable);		
					break;
				case 4:
					imagenes = imagenRepository.findByUsuarioIdClave(idUsuario, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], pageable);		
					break;
				case 5:
					imagenes = imagenRepository.findByUsuarioIdClave(idUsuario, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], listaQuery[4], pageable);		
					break;
				default:
					break;
			}
		}
		return formatearSalida(imagenes);
	}
	
	public Page<ImageDTO> obtenerMiniaturasPorUsuarioDesde(Long idUsuario, LocalDateTime desde, Pageable pageable) {
        
		if (desde == null) {
			return obtenerMiniaturasPorUsuario(idUsuario,pageable);
		}
		Page<Image> imagenes = imagenRepository.findByUsuarioIdFechaDesde(idUsuario, desde, pageable);		
		return formatearSalida(imagenes);
	}

	public Page<ImageDTO> obtenerMiniaturasPorUsuarioHasta(Long idUsuario, LocalDateTime hasta, Pageable pageable) {
        
		if (hasta == null) {
			return obtenerMiniaturasPorUsuario(idUsuario,pageable);
		}
		Page<Image> imagenes = imagenRepository.findByUsuarioIdFechaHasta(idUsuario, hasta, pageable);		
		return formatearSalida(imagenes);
	}

	public Page<ImageDTO> obtenerMiniaturasPorUsuarioDesdeHasta(Long idUsuario, LocalDateTime desde, LocalDateTime hasta, Pageable pageable) {
        
		if (hasta == null || desde == null) {
			return obtenerMiniaturasPorUsuario(idUsuario,pageable);
		}
		Page<Image> imagenes = imagenRepository.findByUsuarioIdFechaDesdeHasta(idUsuario, desde, hasta, pageable);		
		return formatearSalida(imagenes);
	}

	public Page<ImageDTO> obtenerMiniaturasPorUsuarioClavesDesde(Long idUsuario, String query, String busquedaParcial, LocalDateTime desde, Pageable pageable) {

		if (desde == null) {
			return obtenerMiniaturasPorUsuarioClaves(idUsuario, query, busquedaParcial, pageable);
		}

		String[] listaQuery = query.split(";");
		Page<Image> imagenes = null;
		
		if (busquedaParcial.equals("parcial")){
			switch (listaQuery.length) {
				case 1:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeParcial(idUsuario, desde, listaQuery[0], pageable);		
					break;
				case 2:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeParcial(idUsuario, desde, listaQuery[0], listaQuery[1], pageable);		
					break;
				case 3:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeParcial(idUsuario, desde, listaQuery[0], listaQuery[1], listaQuery[2], pageable);		
					break;
				case 4:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeParcial(idUsuario, desde, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], pageable);		
					break;
				case 5:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeParcial(idUsuario, desde, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], listaQuery[4], pageable);		
					break;
				default:
					break;
			}
		} else {
			switch (listaQuery.length) {
				case 1:
					imagenes = imagenRepository.findByUsuarioIdClaveDesde(idUsuario, desde, listaQuery[0], pageable);		
					break;
				case 2:
					imagenes = imagenRepository.findByUsuarioIdClaveDesde(idUsuario, desde, listaQuery[0], listaQuery[1], pageable);		
					break;
				case 3:
					imagenes = imagenRepository.findByUsuarioIdClaveDesde(idUsuario, desde, listaQuery[0], listaQuery[1], listaQuery[2], pageable);		
					break;
				case 4:
					imagenes = imagenRepository.findByUsuarioIdClaveDesde(idUsuario, desde, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], pageable);		
					break;
				case 5:
					imagenes = imagenRepository.findByUsuarioIdClaveDesde(idUsuario, desde, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], listaQuery[4], pageable);		
					break;
				default:
					break;
			}
		}
		return formatearSalida(imagenes);
	}

	public Page<ImageDTO> obtenerMiniaturasPorUsuarioClavesHasta(Long idUsuario, String query, String busquedaParcial, LocalDateTime hasta, Pageable pageable) {

		if (hasta == null) {
			return obtenerMiniaturasPorUsuarioClaves(idUsuario, query, busquedaParcial, pageable);
		}

		String[] listaQuery = query.split(";");
		Page<Image> imagenes = null;
		
		if (busquedaParcial.equals("parcial")) {
			switch (listaQuery.length) {
				case 1:
					imagenes = imagenRepository.findByUsuarioIdClaveHastaParcial(idUsuario, hasta, listaQuery[0], pageable);		
					break;
				case 2:
					imagenes = imagenRepository.findByUsuarioIdClaveHastaParcial(idUsuario, hasta, listaQuery[0], listaQuery[1], pageable);		
					break;
				case 3:
					imagenes = imagenRepository.findByUsuarioIdClaveHastaParcial(idUsuario, hasta, listaQuery[0], listaQuery[1], listaQuery[2], pageable);		
					break;
				case 4:
					imagenes = imagenRepository.findByUsuarioIdClaveHastaParcial(idUsuario, hasta, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], pageable);		
					break;
				case 5:
					imagenes = imagenRepository.findByUsuarioIdClaveHastaParcial(idUsuario, hasta, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], listaQuery[4], pageable);		
					break;
				default:
					break;
			}
		} else {
			switch (listaQuery.length) {
				case 1:
					imagenes = imagenRepository.findByUsuarioIdClaveHasta(idUsuario, hasta, listaQuery[0], pageable);		
					break;
				case 2:
					imagenes = imagenRepository.findByUsuarioIdClaveHasta(idUsuario, hasta, listaQuery[0], listaQuery[1], pageable);		
					break;
				case 3:
					imagenes = imagenRepository.findByUsuarioIdClaveHasta(idUsuario, hasta, listaQuery[0], listaQuery[1], listaQuery[2], pageable);		
					break;
				case 4:
					imagenes = imagenRepository.findByUsuarioIdClaveHasta(idUsuario, hasta, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], pageable);		
					break;
				case 5:
					imagenes = imagenRepository.findByUsuarioIdClaveHasta(idUsuario, hasta, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], listaQuery[4], pageable);		
					break;
				default:
					break;
			}
		}
		
		return formatearSalida(imagenes);
	}

	public Page<ImageDTO> obtenerMiniaturasPorUsuarioClavesDesdeHasta(Long idUsuario, String query, String busquedaParcial, LocalDateTime desde, LocalDateTime hasta, Pageable pageable) {

		if (desde == null || hasta == null) {
			return obtenerMiniaturasPorUsuarioClaves(idUsuario, query, busquedaParcial, pageable);
		}

		String[] listaQuery = query.split(";");
		Page<Image> imagenes = null;
		
		if (busquedaParcial.equals("parcial")) {
			switch (listaQuery.length) {
				case 1:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHastaParcial(idUsuario, desde, hasta, listaQuery[0], pageable);		
					break;
				case 2:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHastaParcial(idUsuario, desde, hasta, listaQuery[0], listaQuery[1], pageable);		
					break;
				case 3:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHastaParcial(idUsuario, desde, hasta, listaQuery[0], listaQuery[1], listaQuery[2], pageable);		
					break;
				case 4:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHastaParcial(idUsuario, desde, hasta, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], pageable);		
					break;
				case 5:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHastaParcial(idUsuario, desde, hasta, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], listaQuery[4], pageable);		
					break;
				default:
					break;
			}
		} else {
			switch (listaQuery.length) {
				case 1:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHasta(idUsuario, desde, hasta, listaQuery[0], pageable);		
					break;
				case 2:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHasta(idUsuario, desde, hasta, listaQuery[0], listaQuery[1], pageable);		
					break;
				case 3:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHasta(idUsuario, desde, hasta, listaQuery[0], listaQuery[1], listaQuery[2], pageable);		
					break;
				case 4:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHasta(idUsuario, desde, hasta, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], pageable);		
					break;
				case 5:
					imagenes = imagenRepository.findByUsuarioIdClaveDesdeHasta(idUsuario, desde, hasta, listaQuery[0], listaQuery[1], listaQuery[2], listaQuery[3], listaQuery[4], pageable);		
					break;
				default:
					break;
			}
		}
		return formatearSalida(imagenes);
	}

	private Page <ImageDTO> formatearSalida(Page<Image> imagenes){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM"); // Define el formato

        return imagenes.map(img -> new ImageDTO(
            img.getRuta(),
            img.getThumbnailUrl(),
            img.getId(),
            img.getFecha() != null ? img.getFecha().format(formatter) : null // Evita el NullPointerException)
        ));
	}

     public List<Image> obtenerImagenesConCoordenadas(Long idUsuario, Double latMin, Double latMax, Double lonMin, Double lonMax) {
        List<Image> imagenes = imagenRepository.findByUsuarioIdAndCoordenadasBetween(idUsuario, latMin, latMax, lonMin, lonMax);
        return imagenes;
    }
     
     public Object obtenerImagenesOClustersService(Long idUsuario, double minLat, double maxLat, double minLon, double maxLon) {
         int totalImagenes = contarImagenes(idUsuario, minLat, maxLat, minLon, maxLon);

         if (totalImagenes > 100) {
             return obtenerClusters(idUsuario, minLat, maxLat, minLon, maxLon);
         } else {
             return obtenerImagenesConCoordenadas(idUsuario,minLat, maxLat, minLon, maxLon);
         }
     }
     
     public List<ClusterDTO> obtenerClusters(Long idUsuario, double lonMin, double lonMax, double latMin, double latMax) {
 	    List<Object[]> resultados = imagenRepository.obtenerClusters(idUsuario, lonMin, lonMax, latMin, latMax);
 	    return resultados.stream().map(obj -> 
 	        new ClusterDTO((Double) obj[1], (Double) obj[0], ((Number) obj[2]).intValue(), 100)
 	    ).toList();
 	}

     public int contarImagenes (Long idUsuario, double minLat, double maxLat, double minLon, double maxLon) {
    	 return imagenRepository.contarImagenesEnArea(idUsuario, minLat, maxLat, minLon, maxLon);
     }

     public void borrarImagenes(List<Long> ids) {
         for (Long id : ids) {
             Optional<Image> imagenOpt = imagenRepository.findById(id);
             if (imagenOpt.isPresent()) {
                 Image imagen = imagenOpt.get();

                 Path rutaOriginal = Paths.get(imagen.getRuta());
                 Path rutaMiniatura = Paths.get(imagen.getThumbnailUrl());

                 try {
                     Files.deleteIfExists(rutaOriginal);
                     Files.deleteIfExists(rutaMiniatura);
                     log.info("Archivos borrados: {} y {}", rutaOriginal, rutaMiniatura);
                 } catch (IOException e) {
                     log.error("Error al borrar archivos de la imagen ID {}: {}", id, e.getMessage());
                     throw new RuntimeException("Error al borrar archivos de imagen con ID: " + id);
                 }

                 imagenRepository.deleteById(id);
                 log.info("Imagen con ID {} eliminada de la base de datos", id);
             } else {
                 log.error("No se encontró imagen con ID {}", id);
                 throw new RuntimeException("Error al borrar archivos de imagen con ID: " + id);
             }
         }
     }

}
