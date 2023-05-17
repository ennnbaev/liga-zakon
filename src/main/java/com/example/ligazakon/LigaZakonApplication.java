package com.example.ligazakon;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableConfigurationProperties
@OpenAPIDefinition
public class LigaZakonApplication {

    public static void main(String[] args) {
        SpringApplication.run(LigaZakonApplication.class, args);
    }

    @Bean
    public ExecutorService getExecutorService(final @Value("${executor.threads.count}") int threadsCount) {
        return Executors.newFixedThreadPool(threadsCount);
    }
}
