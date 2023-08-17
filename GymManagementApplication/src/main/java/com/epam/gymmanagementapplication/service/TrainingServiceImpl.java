package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.request.TrainingReportDTO;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.*;
import com.epam.gymmanagementapplication.model.Trainee;
import com.epam.gymmanagementapplication.model.Trainer;
import com.epam.gymmanagementapplication.model.Training;
import com.epam.gymmanagementapplication.model.TrainingType;
import com.epam.gymmanagementapplication.repository.TrainingRepository;
import com.epam.gymmanagementapplication.util.StringConstants;
import com.epam.gymmanagementapplication.util.ValueMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService{
    private final TrainingRepository trainingRepository;
    private final TrainingTypeServiceImpl trainingTypeService;
    private final UserServiceImpl userService;

    public TrainingReportDTO add(TrainingDetails trainingDetails) throws UserException, TrainingTypeException, TrainerException, TraineeException, TrainingException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), ADD_TRAINING, this.getClass().getName(), trainingDetails.toString());
        Trainer trainer =   Optional.ofNullable(userService.getUser(trainingDetails.getTrainerName()).getTrainer()).orElseThrow(() -> new TrainerException("Trainer Not Found"));
        Trainee trainee =   Optional.ofNullable(userService.getUser(trainingDetails.getTraineeName()).getTrainee()).orElseThrow(() -> new TraineeException("Trainee Not Found"));
        TrainingType trainingType =   Optional.ofNullable(trainingTypeService.get(trainingDetails.getTrainingTypeId())).orElseThrow(() -> new TrainingTypeException("Training Type Not found"));
        if(!(trainee.getTrainerList().contains(trainer)) || !(trainer.getTrainingType().equals(trainingType)))
            throw new TrainingException("Please provide valid data");
        Training training = ValueMapper.createTrainingFrom(trainingDetails, trainer, trainee, trainingType);
        trainingRepository.save(training);
        TrainingReportDTO reportDTO = ValueMapper.createTrainingReportDTOFromTraining(trainer, trainingDetails);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), ADD_TRAINING, this.getClass().getName());
        return reportDTO;
    }


    public List<Training> getTrainingsBetweenForTrainee(Trainee trainee, LocalDate to, LocalDate from, int trainingTypeId) throws TrainingTypeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),GET_TRAININGS_FOR_TRAINEE, this.getClass().getName(), trainee.toString()+" , "+to.toString()+" , "+from.toString()+" , "+trainingTypeId);
        TrainingType trainingType = null;
        if(trainingTypeId != -1)
            trainingType =trainingTypeService.get(trainingTypeId);
        List<Training> trainings = trainingRepository.findAllTrainingInBetween(to,from, trainee, trainingType);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_TRAININGS_FOR_TRAINEE, this.getClass().getName());
        return trainings;
    }

    public List<Training> getTrainingsBetweenForTrainer(Trainer trainer, LocalDate to, LocalDate from,String traineeUsername) throws UserException, TraineeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(),GET_TRAININGS_FOR_TRAINER, this.getClass().getName(), trainer.toString()+" , "+to.toString()+" , "+from.toString()+" , "+traineeUsername);
        Trainee trainee = null;
        if(traineeUsername != null)
            trainee =   Optional.ofNullable(userService.getUser(traineeUsername).getTrainee()).orElseThrow(() -> new TraineeException("No Trainee Found"));
        List<Training> trainings = trainingRepository.findAllTrainingInBetween(to,from, trainer,trainee);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_TRAININGS_FOR_TRAINER, this.getClass().getName());
        return trainings;
    }

}
