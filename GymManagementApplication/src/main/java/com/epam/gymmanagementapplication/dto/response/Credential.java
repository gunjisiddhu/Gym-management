package com.epam.gymmanagementapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {
    private String username;
    private String password;


    @Override
    public String toString() {
        return "username=" + username + '\n' +
                "password=" + password + '\n' ;
    }
}
