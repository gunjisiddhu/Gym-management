package com.epam.gymmanagementapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class TraineeProfile {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private String eMail;
    private boolean isActive;
    private List<TrainerBasicDetails> trainersList;

    @Override
    public String toString() {
        return "First Name='" + firstName + '\'' +
                ", \nLast name='" + lastName + '\'' +
                ", \nDate of Birth =" + dateOfBirth +
                ", \nAddress ='" + address + '\'' +
                ", \nEmail ='" + eMail + '\'' +
                ", \nActive Status =" + isActive +
                ", \nTrainers List=" + trainersList;
    }
}