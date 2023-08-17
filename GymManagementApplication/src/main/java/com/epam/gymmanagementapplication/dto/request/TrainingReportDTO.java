package com.epam.gymmanagementapplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingReportDTO {
    String trainerUsername;
    String trainerFirstName;
    String trainerLastName;
    String eMail;
    boolean trainerStatus;
    LocalDate trainingDate;
    long trainingDuration;
}
