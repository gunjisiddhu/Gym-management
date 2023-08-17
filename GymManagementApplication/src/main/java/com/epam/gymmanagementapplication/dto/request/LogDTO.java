package com.epam.gymmanagementapplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDTO {
    private String fromEmail;
    private List<String> toEmails;
    private List<String> ccEmails;
    private String body;
    private String sentTime;
    private String status;
    private String remarks;
}
