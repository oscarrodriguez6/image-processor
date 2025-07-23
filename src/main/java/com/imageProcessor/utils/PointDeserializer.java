package com.imageProcessor.utils;

import java.io.IOException;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class PointDeserializer extends JsonDeserializer<Point> {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String[] coords = p.getText().split(",");
        double lat = Double.parseDouble(coords[0].trim());
        double lon = Double.parseDouble(coords[1].trim());
        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}
