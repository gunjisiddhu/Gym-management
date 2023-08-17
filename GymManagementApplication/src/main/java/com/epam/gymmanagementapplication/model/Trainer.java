package com.epam.gymmanagementapplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private User user;

    @ManyToMany
    private List<Trainee> traineeList = new ArrayList<>();

    @OneToMany(mappedBy = "trainer")
    private List<Training> trainingList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "Specialization")
    private TrainingType trainingType;

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", user=" + user +
                ", traineeList=" + traineeList +
                ", trainingList=" + trainingList +
                ", trainingType=" + trainingType +
                '}';
    }
}
