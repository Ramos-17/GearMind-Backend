package com.gearmind.gearmind_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.gearmind.gearmind_app.repository")
@EntityScan(basePackages = "com.gearmind.gearmind_app.model")
public class GearmindAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(GearmindAppApplication.class, args);
    }
}

