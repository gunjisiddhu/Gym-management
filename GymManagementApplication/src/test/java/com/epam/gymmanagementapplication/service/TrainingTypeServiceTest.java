package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.request.TrainingTypeDTO;
import com.epam.gymmanagementapplication.exception.TrainingTypeException;
import com.epam.gymmanagementapplication.model.TrainingType;
import com.epam.gymmanagementapplication.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
 class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
     void testGetTrainingType() throws TrainingTypeException {
        // Create mock data
        int trainingTypeId = 1;
        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(trainingTypeId);

        // Mock behavior
        Mockito.when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.of(mockTrainingType));

        // Test
        TrainingType result = trainingTypeService.get(trainingTypeId);

        // Verification
        assertNotNull(result);
        assertEquals(trainingTypeId, result.getId());

        // Verify mock interactions
        Mockito.verify(trainingTypeRepository, Mockito.times(1)).findById(trainingTypeId);
    }

    @Test
     void testGetTrainingTypeNotFound() throws TrainingTypeException {
        // Create mock data
        int trainingTypeId = 1;

        // Mock behavior
        Mockito.when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.empty());

        // Test
        assertThrows(TrainingTypeException.class, () -> trainingTypeService.get(trainingTypeId));

        // Verify mock interactions
        Mockito.verify(trainingTypeRepository, Mockito.times(1)).findById(trainingTypeId);
    }

    @Test
     void testAddTrainingType() {
        // Create mock data
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();
        trainingTypeDTO.setSpecialization("Fitness");

        // Mock behavior
        Mockito.when(trainingTypeRepository.save(Mockito.any(TrainingType.class))).thenReturn(new TrainingType());

        // Test
        TrainingTypeDTO result = trainingTypeService.add(trainingTypeDTO);

        // Verification
        assertNotNull(result);

        // Verify mock interactions
        Mockito.verify(trainingTypeRepository, Mockito.times(1)).save(Mockito.any(TrainingType.class));
    }

}
