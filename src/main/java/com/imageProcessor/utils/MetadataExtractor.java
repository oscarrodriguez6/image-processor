package com.imageProcessor.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.Rational;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.imageProcessor.service.CargaImagenService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataExtractor {
	
	private static final Logger log = LoggerFactory.getLogger(CargaImagenService.class);

    public static Map<String, Object> obtenerMetadatos(File imagen) {
        Map<String, Object> metadataMap = new HashMap<>();

        try {
            Metadata metadata;
            
            metadata = ImageMetadataReader.readMetadata(imagen);
            
            // Metadatos EXIF: Incluye detalles como la fecha y hora de la imagen, la apertura de la lente, la sensibilidad ISO, y más.
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (directory != null) {
                metadataMap.put("ISO", directory.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
                metadataMap.put("Fecha", directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
                metadataMap.put("Apertura", directory.getString(ExifSubIFDDirectory.TAG_FNUMBER));
            }
            
            // Metadatos IPTC: Información como el título, la descripción, el autor, la ciudad, el estado y el país donde se tomó la imagen
            IptcDirectory directory2 = metadata.getFirstDirectoryOfType(IptcDirectory.class);
            if (directory2 != null) {
            	
            	if (directory2.getString(IptcDirectory.TAG_CITY) != null) metadataMap.put("Ciudad", tratarCaracteres(directory2.getString(IptcDirectory.TAG_CITY)));
            	if (directory2.getString(IptcDirectory.TAG_PROVINCE_OR_STATE) != null) metadataMap.put("Provincia", tratarCaracteres(directory2.getString(IptcDirectory.TAG_PROVINCE_OR_STATE)));
            	if (directory2.getString(IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME) != null) metadataMap.put("Pais", tratarCaracteres(directory2.getString(IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME)));
	            
            	String[] keywords = directory2.getStringArray(IptcDirectory.TAG_KEYWORDS);

            	for (int i = 0; i < keywords.length; i++) {
            		metadataMap.put("Etiqueta" + (i + 1),tratarCaracteres(keywords[i]));
            	}

            }
            
            // Metadatos IFD - No es necesaria
//            ExifIFD0Directory directory3 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            try {
	            String coordenadas = getCoordenadas(metadata); 
	            if (coordenadas != null && !coordenadas.equals("")) {
	            	metadataMap.put("Coordenadas", coordenadas);
	            }
            } catch (Exception e) {
				log.error("Error al obtener las coordenadas");
				System.err.println("Error al obtener las coordenadas");
				e.getMessage();
            }

            // Contiene datos sobre la miniatura de la imagen
//            ExifThumbnailDirectory directory4 = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
            
            // Metadatos específicos de Photoshop
//            PhotoshopDirectory directory6 = metadata.getFirstDirectoryOfType(PhotoshopDirectory.class);

            
            
        } catch (Exception e) {
            System.err.println("Error al leer metadatos de " + imagen.getName());
            System.err.println(e.getMessage());
        }

        return metadataMap;
    }
    
    private static String getCoordenadas(Metadata metadata) {
    	
    	String coordenadas = "";
    	try {
	    	GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
	
	        if (gpsDirectory != null) {
	            String latitudeRef = gpsDirectory.getString(GpsDirectory.TAG_LATITUDE_REF);
	            String longitudeRef = gpsDirectory.getString(GpsDirectory.TAG_LONGITUDE_REF);
	            Rational[] latitude = gpsDirectory.getRationalArray(GpsDirectory.TAG_LATITUDE);
	            Rational[] longitude = gpsDirectory.getRationalArray(GpsDirectory.TAG_LONGITUDE);
	
	            double decimalLatitude = convertToDecimal(latitude, latitudeRef);
	            double decimalLongitude = convertToDecimal(longitude, longitudeRef);
	
	            System.out.println("Latitude: " + decimalLatitude);
	            System.out.println("Longitude: " + decimalLongitude);
	            coordenadas = decimalLatitude + " " + decimalLongitude;
	            
	        } else {
	            System.out.println("No se encontraron datos GPS en la imagen.");
	        }
    	} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
        return  coordenadas;
    }

    public static String tratarCaracteres (String palabra) {
    	
    	String palabraTransformada = palabra
                .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U")
                .replace("ñ", "n").replace("Ñ", "N")
                .replace("ç", "c").replace("Ç", "C")
                .replace("ä", "a").replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u")
                .replace("Ä", "A").replace("Ë", "E").replace("Ï", "I").replace("Ö", "O").replace("Ü", "U");; 
    	palabraTransformada = palabraTransformada.toUpperCase();
    	
    	return palabraTransformada;
    }

    private static double convertToDecimal(Rational[] coordinates, String ref) {
        double degrees = coordinates[0].doubleValue() + coordinates[1].doubleValue() / 60 + coordinates[2].doubleValue() / 3600;
        return ref.equals("N") ? degrees : -degrees;
    }

}
