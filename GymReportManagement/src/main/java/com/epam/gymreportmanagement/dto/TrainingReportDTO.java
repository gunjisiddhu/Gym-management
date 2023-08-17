package com.epam.gymreportmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingReportDTO {
    @NotBlank(message = "please provide Trainer Username")
    private String trainerUsername;
    @NotBlank(message = "please provide Trainer First Name")
    private String trainerFirstName;
    @NotBlank(message = "please provide Trainer Last Name")
    private String trainerLastName;
    @NotBlank(message = "please provide E-Mail Address")
    private String eMail;
    @NotNull(message = "please provide Trainer Status")
    private boolean trainerStatus;
    @NotNull(message = "Training date cannot be null")
    @Past(message = "Please provide a date in the past")
    private LocalDate trainingDate;
    @Positive(message = "please provide Duration of Training")
    private long trainingDuration;
}
