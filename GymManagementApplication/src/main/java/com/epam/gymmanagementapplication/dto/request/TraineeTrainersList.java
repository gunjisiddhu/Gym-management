package com.epam.gymmanagementapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainersList {
    @NotBlank(message = "give trainee name")
    private String traineeUsername;
    @NotEmpty(message = "Give atleast one trainer")
    private List<String> trainerUsernameList;
}
