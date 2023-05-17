package com.example.ligazakon;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@OpenAPIDefinition
public class LigaZakonApplication {

	public static void main(String[] args) {
		SpringApplication.run(LigaZakonApplication.class, args);
	}

}
