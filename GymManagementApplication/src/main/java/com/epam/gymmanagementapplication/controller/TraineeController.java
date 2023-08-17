package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.TraineeDetails;
import com.epam.gymmanagementapplication.dto.request.TraineeProfileUpdate;
import com.epam.gymmanagementapplication.dto.request.TraineeTrainersList;
import com.epam.gymmanagementapplication.dto.request.TraineeTrainings;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TraineeProfile;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.*;
import com.epam.gymmanagementapplication.kafka.KafkaProducer;
import com.epam.gymmanagementapplication.service.TraineeService;
import com.epam.gymmanagementapplication.util.StringConstants;
import com.epam.gymmanagementapplication.util.ValueMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/gym/trainee")
@RequiredArgsConstructor
@Slf4j
public class TraineeController {
    private static final String ADD_NEW_TRAINEE = "addNewTrainee";
    private static final String GET_TRAINEE_PROFILE = "getTraineeProfile";
    private static final String UPDATE_TRAINEE_DETAILS = "updateTraineeDetails";
    private static final String REMOVE_TRAINEE = "removeTrainee";
    private static final String UPDATE_TRAINEE_TRAINERS_LIST = "updateTrainers";
    private static final String ADD_NEW_TRAINING = "trainingList";
    private static final String GET_FREE_TRAINERS = "getFreeTrainers";

    private final TraineeService traineeService;
    private final KafkaProducer kafkaProducer;


    @PostMapping("/register")
    ResponseEntity<Credential> addNewTrainee(@Valid @RequestBody TraineeDetails traineeDetails) throws UserException, GymNotificationException, JsonProcessingException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINEE, this.getClass(), traineeDetails.toString());
        Credential credential = traineeService.addNewTrainee(traineeDetails);
        kafkaProducer.sendNotification(ValueMapper.createNotificationDTOFromCredential(credential, traineeDetails.getEMail()));
        ResponseEntity<Credential> response = new ResponseEntity<>(credential, HttpStatus.CREATED);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINEE, this.getClass());
        return response;
    }

    @GetMapping("/")
    ResponseEntity<TraineeProfile> getTraineeProfile(@RequestParam @Valid @NotEmpty(message = "give username") String username) throws TraineeException, UserException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), GET_TRAINEE_PROFILE, this.getClass(), username);
        TraineeProfile traineeProfile = traineeService.getTraineeProfile(username);
        ResponseEntity<TraineeProfile> response = new ResponseEntity<>(traineeProfile, HttpStatus.OK);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), GET_TRAINEE_PROFILE, this.getClass());
        return response;
    }

    @PutMapping("/")
    ResponseEntity<TraineeProfile> updateTraineeDetails(@RequestBody @Valid TraineeProfileUpdate traineeProfileUpdate) throws TraineeException, UserException, GymNotificationException, JsonProcessingException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), UPDATE_TRAINEE_DETAILS, this.getClass(), traineeProfileUpdate.toString());
        TraineeProfile traineeProfile = traineeService.updateTrainee(traineeProfileUpdate);
        kafkaProducer.sendNotification(ValueMapper.createNotificationFromTraineeProfile(traineeProfile));
        ResponseEntity<TraineeProfile> response = new ResponseEntity<>(traineeProfile, HttpStatus.OK);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), UPDATE_TRAINEE_DETAILS, this.getClass());
        return response;
    }

    @DeleteMapping("/")
    ResponseEntity<Void> removeTrainee(@RequestParam @Valid @NotEmpty(message = "give username") String username) throws TraineeException, UserException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), REMOVE_TRAINEE, this.getClass(), username);
        traineeService.removeTrainee(username);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), REMOVE_TRAINEE, this.getClass());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updateTrainers")
    ResponseEntity<List<TrainerProfile>> updateTrainers(@RequestBody @Valid TraineeTrainersList trainersList) throws TrainerException, UserException, TraineeException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), UPDATE_TRAINEE_TRAINERS_LIST, this.getClass(), trainersList.toString());
        List<TrainerProfile> trainerProfiles = traineeService.updateTrainersForTrainee(trainersList);
        ResponseEntity<List<TrainerProfile>> response = new ResponseEntity<>(trainerProfiles,HttpStatus.OK);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), UPDATE_TRAINEE_TRAINERS_LIST, this.getClass());
        return response;
    }

    @PostMapping("/trainings")
    ResponseEntity<List<TrainingDetails>> trainingList(@RequestBody @Valid TraineeTrainings traineeTrainings) throws TraineeException, UserException, TrainingTypeException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINING, this.getClass(), traineeTrainings.toString());
        List<TrainingDetails> trainingDetails = traineeService.getTrainings(traineeTrainings);
        ResponseEntity<List<TrainingDetails>> response = new ResponseEntity<>(trainingDetails,HttpStatus.OK);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINING, this.getClass());
        return response;
    }


    @GetMapping("/freeTrainers")
    ResponseEntity<List<TrainerProfile>> getFreeTrainers(@RequestParam @Valid @NotEmpty(message = "give username") String username) throws UserException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), GET_FREE_TRAINERS, this.getClass(), username);
        List<TrainerProfile> freeTrainers = traineeService.getOtherTrainersOtherThanCurrentTrainee(username);
        ResponseEntity<List<TrainerProfile>> response = new ResponseEntity<>(freeTrainers, HttpStatus.OK);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), GET_FREE_TRAINERS, this.getClass());
        return response;
    }
}
