package com.epam.gymmanagementapplication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDetails {
    @NotBlank(message = "Please give a name")
    String firstName;
    @NotBlank(message = "Please give a name")
    String lastName;
    @NotBlank(message = "please give email")
    @Email(message = "provide valid email address")
    String eMail;
    LocalDate dateOfBirth;
    String address;
}
