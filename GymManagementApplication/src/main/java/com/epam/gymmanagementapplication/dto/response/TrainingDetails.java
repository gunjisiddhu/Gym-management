package com.epam.gymmanagementapplication.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDetails {
    @NotBlank(message = "please provide name")
    String name;
    @NotNull(message = "please provide date")
    LocalDate date;
    @NotNull(message = "please provide type")
    int trainingTypeId;
    @Positive(message = "please provide duration")
    long duration;
    @NotBlank(message = "please provide Trainer Name")
    String trainerName;
    @NotBlank(message = "please provide Trainee Name")
    String traineeName;
}
