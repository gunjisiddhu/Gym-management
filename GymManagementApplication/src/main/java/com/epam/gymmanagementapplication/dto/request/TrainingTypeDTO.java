package com.epam.gymmanagementapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTypeDTO {
    @NotBlank(message = "Give specialization")
    @Pattern(regexp = "Fitness|Yoga|Zumba|Stretching|Resistance", message = "Invalid training type. Allowed values are Fitness, Yoga, Zumba, Stretching, Resistance.")
    String specialization;
}
