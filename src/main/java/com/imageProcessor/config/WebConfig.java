package com.imageProcessor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String VERSION = "?v=" + System.currentTimeMillis();

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);  // Desactiva la caché del navegador

        registry.addResourceHandler("/uploads/miniaturas/**")
                .addResourceLocations("file:uploads/miniaturas/")
                .setCachePeriod(0);  // También desactiva la caché para miniaturas
    }

//    public static String getStaticResourceUrl(String resourcePath) {
//       return "/uploads/miniaturas/" + resourcePath + VERSION;
//    }
}
