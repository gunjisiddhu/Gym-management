package com.epam.gymmanagementapplication.controller;


import com.epam.gymmanagementapplication.dto.request.TrainingReportDTO;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.*;
import com.epam.gymmanagementapplication.kafka.KafkaProducer;
import com.epam.gymmanagementapplication.service.TrainingService;
import com.epam.gymmanagementapplication.util.StringConstants;
import com.epam.gymmanagementapplication.util.ValueMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gym/training")
@Slf4j
@RequiredArgsConstructor
public class TrainingController {
    private static final String ADD_NEW_TRAINING = "addNewTraining";
    private final TrainingService trainingService;
    private final KafkaProducer kafkaProducer;


    @PostMapping("/")
    ResponseEntity<Void> addNewTraining(@RequestBody @Valid TrainingDetails trainingDetails) throws TrainingTypeException, UserException, TraineeException, TrainerException, TrainingException, GymNotificationException, JsonProcessingException {
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINING, this.getClass(), trainingDetails.toString());
        TrainingReportDTO reportDTO = trainingService.add(trainingDetails);
        kafkaProducer.sendNotification(ValueMapper.createNotificationFromReportDTO(reportDTO));
        kafkaProducer.sendTrainingReport(reportDTO);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINING, this.getClass());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
