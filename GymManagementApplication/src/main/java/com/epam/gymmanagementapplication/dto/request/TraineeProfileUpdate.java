package com.epam.gymmanagementapplication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeProfileUpdate {
    @NotBlank(message = "please give your username")
    private String username;
    @NotBlank(message = "Please give a name")
    private String firstName;
    @NotBlank(message = "Please give a name")
    private String lastName;
    @NotBlank(message = "Please give a name")
    @Email(message = "please provide valid mail")
    private String eMail;
    @NotNull(message = "Give date of birth")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Give proper address")
    private String address;
    @NotNull
    private boolean isActive;
}
