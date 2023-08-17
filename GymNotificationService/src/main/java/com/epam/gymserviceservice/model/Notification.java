package com.epam.gymserviceservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    private String id;
    private String fromEmail;
    private List<String> toEmails;
    private List<String> ccEmails;
    private String body;
    private String sentTime;
    private String status;
    private String remarks;
}
