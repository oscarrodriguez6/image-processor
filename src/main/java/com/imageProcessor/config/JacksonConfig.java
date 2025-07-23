package com.imageProcessor.config;

import org.locationtech.jts.geom.Point;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.imageProcessor.utils.PointDeserializer;
import com.imageProcessor.utils.PointSerializer;

@Configuration
public class JacksonConfig {

    @Bean
    public Module pointModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Point.class, new PointSerializer());
        module.addDeserializer(Point.class, new PointDeserializer());
        return module;
    }
}
