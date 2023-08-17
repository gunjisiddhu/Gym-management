package com.epam.gymmanagementapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    @NotBlank(message = "please provide from email")
    private String fromEmail;
    @NotEmpty(message = "please provide to emails list")
    private List<String> toEmails;
    @NotNull(message = "please provide cc emails list")
    private List<String> ccEmails;
    @NotBlank(message = "please provide body")
    private String subject;
    @NotBlank(message = "please provide body")
    private String body;
}
