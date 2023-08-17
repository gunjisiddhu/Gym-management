package com.epam.gymmanagementapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    int id;
    String firstName;
    String lastName;
    String username;
    String password;
    String eMail;
    boolean isActive;
}
