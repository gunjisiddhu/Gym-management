package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.TrainingTypeDTO;
import com.epam.gymmanagementapplication.service.TrainingTypeService;
import com.epam.gymmanagementapplication.util.StringConstants;
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
@RequestMapping("/gym/trainingType")
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    public static final String ADD_NEW_TRAINING_TYPE = "addNewTrainingType";
    @PostMapping("/")
    ResponseEntity<Void> addNewTrainingType(@RequestBody @Valid TrainingTypeDTO trainingTypeDTO){
        log.info(StringConstants.ENTERED_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINING_TYPE, this.getClass().getName(), trainingTypeDTO.toString());
        trainingTypeService.add(trainingTypeDTO);
        log.info(StringConstants.EXITING_CONTROLLER_MESSAGE.getValue(), ADD_NEW_TRAINING_TYPE, this.getClass().getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
