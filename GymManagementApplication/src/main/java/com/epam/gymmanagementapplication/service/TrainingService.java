package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.request.TrainingReportDTO;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.*;
import com.epam.gymmanagementapplication.model.Trainee;
import com.epam.gymmanagementapplication.model.Trainer;
import com.epam.gymmanagementapplication.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
    String ADD_TRAINING = "add";
    String GET_TRAININGS_FOR_TRAINEE = "getTrainingsBetweenForTrainee";
    String GET_TRAININGS_FOR_TRAINER = "getTrainingsBetweenForTrainer";

    TrainingReportDTO add(TrainingDetails trainingDetails) throws UserException, TrainingTypeException, TrainerException, TraineeException, TrainingException;


    List<Training> getTrainingsBetweenForTrainee(Trainee trainee, LocalDate to, LocalDate from, int trainingTypeId) throws TrainingTypeException;

    List<Training> getTrainingsBetweenForTrainer(Trainer trainer, LocalDate to, LocalDate from, String traineeUsername) throws UserException, TraineeException;

}
