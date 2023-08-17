package com.epam.gymserviceservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class GymNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymNotificationServiceApplication.class, args);
    }

}
