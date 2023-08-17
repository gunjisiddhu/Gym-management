package com.epam.gymmanagementapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Pattern(regexp = "Fitness|Yoga|Zumba|Stretching|Resistance", message = "Invalid training type. Allowed values are Fitness, Yoga, Zumba, Stretching, Resistance.")
    private String trainingTypeName;

    @OneToMany(mappedBy = "trainingType")
    private List<Trainer> trainerList = new ArrayList<>();

    @OneToMany(mappedBy = "trainingType")
    private List<Training> trainingList = new ArrayList<>();

    @Override
    public String toString() {
        return "TrainingType{" +
                "id=" + id +
                ", trainingTypeName='" + trainingTypeName + '\'' +
                ", trainingList=" + trainingList +
                '}';
    }
}
