package com.epam.gymmanagementapplication.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraineeBasicDetails {
    private String username;
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return  "*** Username='" + username + '\'' +
                ",\n    FirstName='" + firstName + '\'' +
                ",\n    LastName='" + lastName + '\'';
    }
}
