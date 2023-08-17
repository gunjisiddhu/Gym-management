package com.epam.gymmanagementapplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String trainingName;
    @Column(nullable = false)
    private LocalDate trainingDate;
    @Column(nullable = false)
    private Long trainingDuration;

    @ManyToOne
    private Trainee trainee;

    @ManyToOne
    private Trainer trainer;

    @ManyToOne
    private TrainingType trainingType;
}
