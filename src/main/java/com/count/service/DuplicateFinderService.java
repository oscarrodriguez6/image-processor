package com.count.service;

import org.springframework.stereotype.Service;

import com.count.dto.ResultadoDuplicadosDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.sql.Time;
import java.util.*;

@Service
public class DuplicateFinderService {

    /**
     * Punto de entrada del servicio. Busca archivos en la ruta dada.
     * @param ruta Ruta del directorio raíz donde buscar
     * @return Mapa de duplicados: archivo original → contadores
     */
    public ResultadoDuplicadosDTO contarArchivos(String ruta, String subcarpetaExcluida) {

        long inicio = System.nanoTime();
    	
     	// Paso 1: Agrupar archivos por tamaño (los de distinto tamaño no pueden ser iguales)
        List<Integer> buscarArchivos = buscarArchivos(ruta, subcarpetaExcluida);

        System.out.println("Resultados:");
        System.out.println("Imágenes encontradas: " + buscarArchivos.get(0));
        System.out.println("Vídeos encontrados: " + buscarArchivos.get(1));
        System.out.println("Otros archivos encontrados: " + buscarArchivos.get(2));
        // Armar el DTO
        ResultadoDuplicadosDTO resultado = new ResultadoDuplicadosDTO();
        resultado.setImagenes(buscarArchivos.get(0));
        resultado.setVideos(buscarArchivos.get(1));
        resultado.setOtros(buscarArchivos.get(2));
        resultado.setDuplicados(null);

        long fin = System.nanoTime();
        long tiempoTotal = fin - inicio;
        System.out.println("El método tardó: " + tiempoTotal / 1_000_000 + " ms");

        inicio = System.nanoTime();
    	
     	// Paso 1: Agrupar archivos por tamaño (los de distinto tamaño no pueden ser iguales)
        List<Integer> lista = new ArrayList<>();
        lista.add(0);
        lista.add(0);
        lista.add(0);
        buscarArchivos2(ruta, subcarpetaExcluida,lista);

        System.out.println("Resultados:");
        System.out.println("Imágenes encontradas: " + buscarArchivos.get(0));
        System.out.println("Vídeos encontrados: " + buscarArchivos.get(1));
        System.out.println("Otros archivos encontrados: " + buscarArchivos.get(2));


        fin = System.nanoTime();
        tiempoTotal = fin - inicio;
        System.out.println("El método tardó: " + tiempoTotal / 1_000_000 + " ms");

        return resultado;

    }

	private List<Integer> buscarArchivos(String ruta, String subcarpetaExcluida){

    	List<Integer> archivosEncontrados = new ArrayList<>();
    	archivosEncontrados.add(0);
    	archivosEncontrados.add(0);
    	archivosEncontrados.add(0);

    	try {
    		Files.walk(Paths.get(ruta))
    			.filter(path -> !path.toString().contains(subcarpetaExcluida)) // Filtra el directorio a omitir
    			.forEach(path -> {
    				if (Files.isDirectory(path)) {
    					System.out.println("Carpeta tratada: " + path.toString());
    				}
    				if (Files.isRegularFile(path)) {
    					if (esImagen(path.toString().toLowerCase())) archivosEncontrados.set(0, archivosEncontrados.get(0) + 1);
   	                 	else if (esVideo(path.toString().toLowerCase())) archivosEncontrados.set(1, archivosEncontrados.get(1) + 1);
   	                 	else archivosEncontrados.set(2, archivosEncontrados.get(2) + 1);
   				}

    			});
    		} catch (Exception e) {
				System.out.println("Error al tratar los archivos");
			} 
    	return archivosEncontrados;
    }
	private void buscarArchivos2(String ruta, String subcarpetaExcluida, List<Integer> lista){

    	File carpeta = new File(ruta);
    	
        File[] archivos = carpeta.listFiles();
        if (archivos == null) return;

        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                // Si es una carpeta, buscar imágenes dentro de ella (recursividad)
            	if (archivo.getAbsolutePath().startsWith(subcarpetaExcluida))
            		System.out.println("Carpeta excluida: " + archivo.getAbsolutePath());
            	else
            		buscarArchivos2(archivo.getAbsolutePath(), subcarpetaExcluida,lista);
            } else {
            	if (esImagen(archivo.getAbsolutePath().toString().toLowerCase())) lista.set(0, lista.get(0) + 1);
            	else if (esVideo(archivo.getAbsolutePath().toString().toLowerCase())) lista.set(1, lista.get(1) + 1);
            	else lista.set(2, lista.get(2) + 1);

            }
        }
    	return;
    }
    
    // Comprueba si el archivo es imagen
    private boolean esImagen(String nombre) {
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png")
            || nombre.endsWith(".gif") || nombre.endsWith(".bmp") || nombre.endsWith(".webp");
    }

    // Comprueba si el archivo es vídeo
    private boolean esVideo(String nombre) {
        return nombre.endsWith(".mp4") || nombre.endsWith(".avi") || nombre.endsWith(".mov")
            || nombre.endsWith(".mkv") || nombre.endsWith(".wmv") || nombre.endsWith(".flv");
    }

    /**
     * Punto de entrada del servicio. Busca archivos duplicados en la ruta dada.
     * @param ruta Ruta del directorio raíz donde buscar
     * @return Mapa de duplicados: archivo original → lista de duplicados
     */
    public ResultadoDuplicadosDTO buscarDuplicados(String ruta, String subcarpetaExcluida) {
        // Paso 1: Agrupar archivos por tamaño (los de distinto tamaño no pueden ser iguales)
        Map<Long, List<Path>> archivosPorTamaño = agruparPorTamaño(ruta, subcarpetaExcluida);

        // Paso 2: Para archivos con el mismo tamaño, comparar su contenido por hash
        Map<String, List<String>> duplicados = encontrarDuplicadosPorHash(archivosPorTamaño);
        
        // Contar tipos de archivo
        int imagenes = 0, videos = 0, otros = 0;

        for (List<Path> paths : archivosPorTamaño.values()) {
            for (Path path : paths) {
                String nombre = path.toString().toLowerCase();
                if (esImagen(nombre)) imagenes++;
                else if (esVideo(nombre)) videos++;
                else otros++;
            }
        }

        System.out.println("Resultados:");
        System.out.println("Imágenes encontradas: " + imagenes);
        System.out.println("Vídeos encontrados: " + videos);
        System.out.println("Otros archivos encontrados: " + otros);
        System.out.println("Duplicados encontrados: " + duplicados.size());
        // Armar el DTO
        ResultadoDuplicadosDTO resultado = new ResultadoDuplicadosDTO();
        resultado.setImagenes(imagenes);
        resultado.setVideos(videos);
        resultado.setOtros(otros);
        resultado.setDuplicados(duplicados);

        return resultado;

    }

    /**
     * Recorre recursivamente todos los archivos de la ruta y los agrupa por tamaño.
     * @param ruta Carpeta a analizar
     * @return Mapa: tamaño en bytes → lista de archivos con ese tamaño
     */
    private Map<Long, List<Path>> agruparPorTamaño(String ruta, String subcarpetaExcluida) {
        Map<Long, List<Path>> agrupados = new HashMap<>();

        try {
            Files.walkFileTree(Paths.get(ruta), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    // Comprobar si la carpeta actual es la subcarpeta que se quiere excluir
                    if (dir.endsWith(subcarpetaExcluida)) {
                        System.out.println("🚫 Excluyendo carpeta: " + dir.toAbsolutePath());
                        return FileVisitResult.SKIP_SUBTREE; // Omitir esta carpeta y su contenido
                    }                    System.out.println("🔍 Entrando en carpeta: " + dir.toAbsolutePath());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (Files.isRegularFile(file)) {
                        try {
                            long tamaño = Files.size(file);
                            agrupados.computeIfAbsent(tamaño, k -> new ArrayList<>()).add(file);
                        } catch (IOException e) {
                            System.err.println("⚠️ Error al leer tamaño de " + file + ": " + e.getMessage());
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error al recorrer la carpeta", e);
        }

        return agrupados;
    }


    /**
     * Dentro de cada grupo de archivos con el mismo tamaño, compara su contenido mediante hash.
     * @param archivosPorTamaño Mapa de archivos agrupados por tamaño
     * @return Mapa: archivo original → lista de duplicados
     */
    private Map<String, List<String>> encontrarDuplicadosPorHash(Map<Long, List<Path>> archivosPorTamaño) {
        Map<String, List<String>> duplicados = new HashMap<>();

        System.out.println("Entra a comprobar duplicados");
        System.out.println("Grupos a tratar en al buscar duplicados: " + archivosPorTamaño.size());
    	int i = 0;
    	int j = 0;
        
        for (List<Path> grupo : archivosPorTamaño.values()) {
        	i++;j++;
        	if (i == 1000) {
        		System.out.println("Grupos tratados: " + j);
        		i = 0;
        	}
            // Si solo hay un archivo con ese tamaño, no puede ser duplicado
            if (grupo.size() < 2) continue;

            // Mapa para agrupar archivos por hash
            Map<String, List<String>> hashToPaths = new HashMap<>();

            for (Path archivo : grupo) {
                try {
                    String hash = calcularHash(archivo); // Hash del contenido
                    hashToPaths.computeIfAbsent(hash, k -> new ArrayList<>()).add(archivo.toString());
                } catch (Exception e) {
                    System.err.println("Error procesando archivo " + archivo + ": " + e.getMessage());
                }
            }

            // Solo nos interesan los hashes que tienen más de un archivo (duplicados)
            for (List<String> archivosMismoHash : hashToPaths.values()) {
                if (archivosMismoHash.size() > 1) {
                    // El primero es el "original", los demás se consideran duplicados
                    duplicados.put(archivosMismoHash.get(0), archivosMismoHash.subList(1, archivosMismoHash.size()));
                }
            }
        }

        return duplicados;
    }

    /**
     * Calcula el hash MD5 de un archivo leyendo su contenido completo.
     * Es más rápido que SHA-256 y suficiente para comparación de archivos.
     * @param path Ruta del archivo
     * @return Hash hexadecimal como String
     */
    private String calcularHash(Path path) throws IOException, NoSuchAlgorithmException {
    	long inicio = System.nanoTime();
        MessageDigest digest = MessageDigest.getInstance("MD5"); // Algoritmo rápido
        byte[] buffer = new byte[8192]; // Lectura en bloques (8 KB)
        try (InputStream is = Files.newInputStream(path)) {
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead); // Alimentar el hash
            }
        }

        // Convertir el resultado del hash (array de bytes) a hexadecimal
        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        long fin = System.nanoTime();
        long tiempoTotal = fin - inicio;
        System.out.println("El método tardó: " + tiempoTotal / 1_000_000 + " ms");
        return sb.toString();
    }
}
