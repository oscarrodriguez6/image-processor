package com.webapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PaisesUtil {

    private static Properties properties = new Properties();

    static {
        try (InputStream inputStream = PaisesUtil.class.getClassLoader().getResourceAsStream("static/properties/banderas.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de países: " + e.getMessage());
        }
    }

    public static String getCountryCode(String countryName) {
    	return properties.getProperty(countryName);
    }
}
