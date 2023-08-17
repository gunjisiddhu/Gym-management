package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.request.TrainingTypeDTO;
import com.epam.gymmanagementapplication.exception.TrainingTypeException;
import com.epam.gymmanagementapplication.model.TrainingType;

public interface TrainingTypeService {
    String GET_TRAINING_TYPE = "get";
    String ADD_TRAINING_TYPE = "add";

    TrainingType get(int id) throws TrainingTypeException;
    TrainingTypeDTO add(TrainingTypeDTO trainingTypeDTO);
}
