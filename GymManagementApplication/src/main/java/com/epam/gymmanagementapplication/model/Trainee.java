package com.epam.gymmanagementapplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateOfBirth;
    private String address;

    @OneToOne
    private User user;

    @ManyToMany(mappedBy = "traineeList")
    private List<Trainer> trainerList;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
    private List<Training> trainingList;

    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + id +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", user=" + user +
                ", trainingList=" + trainingList +
                '}';
    }
}
