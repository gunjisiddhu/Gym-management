package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.request.TrainingTypeDTO;
import com.epam.gymmanagementapplication.exception.TrainingTypeException;
import com.epam.gymmanagementapplication.model.TrainingType;
import com.epam.gymmanagementapplication.repository.TrainingTypeRepository;
import com.epam.gymmanagementapplication.util.StringConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeServiceImpl implements TrainingTypeService{
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingType get(int id) throws TrainingTypeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_TRAINING_TYPE, this.getClass().getName(), ""+id);
        TrainingType trainingType = trainingTypeRepository.findById(id).orElseThrow(() -> new TrainingTypeException("No training found"));
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_TRAINING_TYPE, this.getClass().getName());
        return trainingType;
    }


    public TrainingTypeDTO add(TrainingTypeDTO trainingTypeDTO){
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), ADD_TRAINING_TYPE, this.getClass().getName(), trainingTypeDTO.toString());
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(trainingTypeDTO.getSpecialization());
        trainingTypeRepository.save(trainingType);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), ADD_TRAINING_TYPE, this.getClass().getName());
        return trainingTypeDTO;
    }
}
