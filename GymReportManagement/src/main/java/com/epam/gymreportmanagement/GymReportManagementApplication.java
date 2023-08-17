package com.epam.gymreportmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class GymReportManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymReportManagementApplication.class, args);
    }

}
