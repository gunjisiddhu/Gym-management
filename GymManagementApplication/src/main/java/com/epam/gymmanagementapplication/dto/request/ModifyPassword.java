package com.epam.gymmanagementapplication.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPassword {
    @NotBlank(message = "Give username")
    private String username;
    @NotBlank(message = "Give old password")
    private String oldPassword;
    @NotBlank(message = "Give new password")

    private String newPassword;
}
