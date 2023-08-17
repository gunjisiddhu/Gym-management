package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.TrainerDetails;
import com.epam.gymmanagementapplication.dto.request.TrainerProfileUpdate;
import com.epam.gymmanagementapplication.dto.request.TrainerTrainings;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.*;
import com.epam.gymmanagementapplication.kafka.KafkaProducer;
import com.epam.gymmanagementapplication.service.TrainerService;
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
@RequestMapping("/gym/trainer")
@RequiredArgsConstructor
@Slf4j
public class TrainerController {
    private static final String ADD_NEW_TRAINER = "addNewTrainer";
    private static final String GET_TRAINER_PROFILE = "getTrainerProfile";
    private static final String UPDATE_TRAINER_DETAILS = "updateTrainerDetails";
    private static final String GET_TRAINER_TRAININGS = "getTrainings";

    private final TrainerService trainerService;
    private final KafkaProducer kafkaProducer;

    @PostMapping("/register")
    ResponseEntity<Credential> addNewTrainer(@Valid @RequestBody TrainerDetails trainerDetails) throws TrainingTypeException, UserException, GymNotificationException, JsonProcessingException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINER, this.getClass(), trainerDetails.toString());
        Credential credential = trainerService.addTrainer(trainerDetails);
        kafkaProducer.sendNotification(ValueMapper.createNotificationDTOFromCredential(credential, trainerDetails.getEMail()));
        ResponseEntity<Credential> response = new ResponseEntity<>(credential, HttpStatus.CREATED);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINER, this.getClass());
        return response;
    }

    @GetMapping("/")
    ResponseEntity<TrainerProfile> getTrainerProfile(@RequestParam @Valid @NotEmpty(message = "give username") String username) throws TrainerException, UserException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), GET_TRAINER_PROFILE, this.getClass(), username);
        TrainerProfile trainerProfile = trainerService.getTrainerProfile(username);
        ResponseEntity<TrainerProfile> response = new ResponseEntity<>(trainerProfile, HttpStatus.OK);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), GET_TRAINER_PROFILE, this.getClass());
        return response;
    }

    @PutMapping("/")
    ResponseEntity<TrainerProfile> updateTrainerDetails(@RequestBody @Valid TrainerProfileUpdate trainerProfileUpdate) throws TrainingTypeException, TrainerException, UserException, GymNotificationException, JsonProcessingException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), UPDATE_TRAINER_DETAILS, this.getClass(), trainerProfileUpdate.toString());
        TrainerProfile trainerProfile = trainerService.updateTrainer(trainerProfileUpdate);
        kafkaProducer.sendNotification(ValueMapper.createNotificationFromTrainerProfile(trainerProfile));
        ResponseEntity<TrainerProfile> response = new ResponseEntity<>(trainerProfile, HttpStatus.OK);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), UPDATE_TRAINER_DETAILS, this.getClass());
        return response;
    }

    @PostMapping("/trainingList")
    ResponseEntity<List<TrainingDetails>> getTrainings(@RequestBody @Valid TrainerTrainings trainerTrainings) throws TraineeException, UserException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), GET_TRAINER_TRAININGS, this.getClass(), trainerTrainings.toString());
        List<TrainingDetails> trainingDetails = trainerService.getTrainings(trainerTrainings);
        ResponseEntity<List<TrainingDetails>> response = new ResponseEntity<>(trainingDetails, HttpStatus.OK);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), GET_TRAINER_TRAININGS, this.getClass());
        return response;
    }

}
