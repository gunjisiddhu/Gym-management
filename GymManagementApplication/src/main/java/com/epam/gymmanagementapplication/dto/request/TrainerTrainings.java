package com.epam.gymmanagementapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TrainerTrainings {
    @NotBlank(message = "please provide username")
    private String username;
    private LocalDate fromDate;
    private LocalDate endDate;
    private String traineeName;
}
