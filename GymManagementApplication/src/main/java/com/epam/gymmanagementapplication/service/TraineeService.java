package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.request.TraineeDetails;
import com.epam.gymmanagementapplication.dto.request.TraineeProfileUpdate;
import com.epam.gymmanagementapplication.dto.request.TraineeTrainersList;
import com.epam.gymmanagementapplication.dto.request.TraineeTrainings;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TraineeProfile;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.TraineeException;
import com.epam.gymmanagementapplication.exception.TrainerException;
import com.epam.gymmanagementapplication.exception.TrainingTypeException;
import com.epam.gymmanagementapplication.exception.UserException;

import java.util.List;

public interface TraineeService {

    String ADD_NEW_TRAINEE = "addNewTrainee";
    String GET_TRAINEE_PROFILE = "getTraineeProfile";
    String UPDATE_TRAINEE = "updateTrainee";
    String REMOVE_TRAINEE = "removeTrainee";
    String UPDATE_TRAINERS_FOR_TRAINEE = "updateTrainersForTrainee";
    String GET_OTHER_ACTIVE_TRAINERS = "getOtherTrainersOtherThanCurrentTrainee";
    String GET_TRAININGS = "getTrainings";


    Credential addNewTrainee(TraineeDetails traineeDetails) throws UserException;
    TraineeProfile getTraineeProfile(String username) throws UserException;
    TraineeProfile updateTrainee(TraineeProfileUpdate traineeProfileUpdate) throws UserException, TraineeException;
    void removeTrainee(String username) throws UserException, TraineeException;
    List<TrainerProfile> updateTrainersForTrainee(TraineeTrainersList trainersList) throws UserException, TrainerException, TraineeException;
    List<TrainerProfile> getOtherTrainersOtherThanCurrentTrainee(String username) throws UserException;
    List<TrainingDetails> getTrainings(TraineeTrainings traineeTrainings) throws UserException, TraineeException, TrainingTypeException;
}
