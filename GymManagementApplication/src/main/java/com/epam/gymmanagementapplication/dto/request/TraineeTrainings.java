package com.epam.gymmanagementapplication.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@AllArgsConstructor
public class TraineeTrainings {
    @NotBlank(message = "please provide username")
    private String username;
    private LocalDate fromDate;
    private LocalDate endDate;
    private String trainerName;
    private int trainingTypeId = -1;
}
