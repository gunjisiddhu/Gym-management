package com.epam.gymmanagementapplication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileUpdate {
    @NotBlank(message = "Give username")
    String username;
    @NotBlank(message = "Give firstname")
    String firstName;
    @NotBlank(message = "Give lastname")
    String lastName;
    @NotBlank(message = "Give lastname")
    @Email(message = "Give valid Email")
    String eMail;
    @NotNull(message = "Give specialization ID")
    int specializationId;
    @NotNull
    boolean isActive;
}
