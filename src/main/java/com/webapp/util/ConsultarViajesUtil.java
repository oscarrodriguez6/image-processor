package com.webapp.util;

import java.time.temporal.ChronoUnit;

import org.locationtech.jts.geom.Point;
import com.imageProcessor.model.Viaje;

public class ConsultarViajesUtil {

    private static final double RADIO_TIERRA_KM = 6371.0;
	
	public static int calcularDiasViaje(Viaje viaje) {
		if (viaje.getFecha_inicio() != null && viaje.getFecha_fin() != null) {
			long tiempo = ChronoUnit.DAYS.between(viaje.getFecha_inicio().toLocalDate(), viaje.getFecha_fin().toLocalDate());
			if (viaje.getFecha_fin().getHour() > viaje.getFecha_inicio().getHour() || viaje.getFecha_fin().getMinute() > viaje.getFecha_inicio().getMinute()) {
	            tiempo++;
	        }
			return (int) tiempo;
		} else {
			return 0;
		}
	}

	public static Long calcularKmPaso(Point puntoNuevo, Point puntoAnt) {
		if (puntoNuevo != null && puntoAnt != null) {
			double lat1 = puntoAnt.getY();
			double lon1 = puntoAnt.getX();
			double lat2 = puntoNuevo.getY();
			double lon2 = puntoNuevo.getX();
			        double dLat = Math.toRadians(lat2 - lat1);
			        double dLon = Math.toRadians(lon2 - lon1);

			        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
			                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);

			        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

			        double distanciaD = RADIO_TIERRA_KM * c;
			        Long distancia = Math.round(distanciaD);
			        return distancia;
		} else {
			return (long) 0;
		}
	}

}
