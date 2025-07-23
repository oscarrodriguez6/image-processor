package com.imageProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.imageProcessor", "com.webapp", "com.count"})
public class ImageProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageProcessorApplication.class, args);
	}

}
