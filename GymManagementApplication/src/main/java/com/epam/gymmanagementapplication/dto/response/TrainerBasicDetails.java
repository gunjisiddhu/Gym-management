package com.epam.gymmanagementapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TrainerBasicDetails {
    private String username;
    private String firstName;
    private String secondName;
    private String specialization;

    @Override
    public String toString() {
        return "*** username='" + username + '\'' +
                ", \n    firstName='" + firstName + '\'' +
                ", \n    secondName='" + secondName + '\'' +
                ", \n    specialization='" + specialization + '\'';
    }
}
