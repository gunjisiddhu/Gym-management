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
public class TrainerDetails {
    @NotBlank(message = "please provide first name")
    String firstName;
    @NotBlank(message = "please provide first name")
    String lastName;
    @NotBlank(message = "please provide email")
    @Email(message = "provide valid email address")
    String eMail;
    @NotNull(message = "please provide specialization id")
    int specializationId;
}
