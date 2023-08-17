package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.request.TrainerDetails;
import com.epam.gymmanagementapplication.dto.request.TrainerProfileUpdate;
import com.epam.gymmanagementapplication.dto.request.TrainerTrainings;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.TraineeException;
import com.epam.gymmanagementapplication.exception.TrainerException;
import com.epam.gymmanagementapplication.exception.TrainingTypeException;
import com.epam.gymmanagementapplication.exception.UserException;
import com.epam.gymmanagementapplication.model.Trainer;

import java.util.List;

public interface TrainerService {
    String ADD_TRAINER = "addTrainer";
    String GET_TRAINER_PROFILE = "getTrainerProfile";
    String UPDATE_TRAINER = "updateTrainer";
    String GET_TRAININGS = "getTrainings";
    String GET_TRAINING_DETAILS = "getTrainingDetails";


    Credential addTrainer(TrainerDetails trainerDetails) throws TrainingTypeException, UserException;
    TrainerProfile getTrainerProfile(String username) throws UserException, TrainerException;
    TrainerProfile updateTrainer(TrainerProfileUpdate trainerProfileUpdate) throws UserException, TrainerException, TrainingTypeException;
    List<TrainingDetails> getTrainings(TrainerTrainings trainerTrainings) throws UserException, TraineeException;

    List<Trainer> fetchAllTrainers();
}
