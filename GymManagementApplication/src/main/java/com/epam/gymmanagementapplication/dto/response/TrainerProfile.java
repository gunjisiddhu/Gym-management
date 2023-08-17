package com.epam.gymmanagementapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TrainerProfile {
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private String eMail;
    private List<TraineeBasicDetails> traineeList;

    @Override
    public String toString() {
        return  "First Name='" + firstName + '\'' +
                ", \nLast Name='" + lastName + '\'' +
                ", \nSpecialization='" + specialization + '\'' +
                ", \nActive Status=" + isActive +
                ", \nEmail='" + eMail + '\'' +
                ", \nTrainee List=" + traineeList;
    }
}
