package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.request.TrainingReportDTO;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.*;
import com.epam.gymmanagementapplication.model.*;
import com.epam.gymmanagementapplication.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    TrainingRepository trainingRepository;
    @Mock
    TrainingTypeServiceImpl trainingTypeService;
    @Mock
    UserServiceImpl userService;

    @InjectMocks
    TrainingServiceImpl trainingService;


    @Test
    void testAddTraining() throws UserException, TrainingTypeException, TrainerException, TraineeException, TrainingException {
        // Create mock data
        TrainingDetails trainingDetails = new TrainingDetails();
        trainingDetails.setTrainerName("pavs");
        trainingDetails.setTraineeName("sid");
        trainingDetails.setTrainingTypeId(1);

        User mockTrainerUser = new User();
        mockTrainerUser.setUsername("pavs");
        mockTrainerUser.setFirstName("Ammu");
        mockTrainerUser.setLastName("Pavani");
        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockTrainerUser);
        mockTrainerUser.setTrainer(mockTrainer);

        User mockTraineeUser = new User();
        mockTraineeUser.setUsername("sid");
        mockTraineeUser.setFirstName("Siddhu");
        mockTraineeUser.setLastName("Saai");
        Trainee mockTrainee = new Trainee();
        mockTrainee.setUser(mockTraineeUser);
        mockTraineeUser.setTrainee(mockTrainee);


        mockTrainee.setTrainerList(List.of(mockTrainer));
        mockTrainer.setTraineeList(List.of(mockTrainee));


        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);
        mockTrainingType.setTrainingTypeName("Zumba");

        mockTrainer.setTrainingType(mockTrainingType);

        // Mock behavior
        Mockito.when(userService.getUser("pavs")).thenReturn(mockTrainerUser);
        Mockito.when(userService.getUser("sid")).thenReturn(mockTraineeUser);
        Mockito.when(trainingTypeService.get(1)).thenReturn(mockTrainingType);
        Mockito.when(trainingRepository.save(Mockito.any(Training.class))).thenReturn(new Training());

        // Test
        TrainingReportDTO result = trainingService.add(trainingDetails);

        // Verification
        assertNotNull(result);

        // Verify mock interactions
        Mockito.verify(userService, Mockito.times(1)).getUser("pavs");
        Mockito.verify(userService, Mockito.times(1)).getUser("sid");
        Mockito.verify(trainingTypeService, Mockito.times(1)).get(1);
        Mockito.verify(trainingRepository, Mockito.times(1)).save(Mockito.any(Training.class));
    }


    @Test
    void testAddTrainingTrainingException1() throws UserException, TrainingTypeException, TrainerException, TraineeException, TrainingException {
        // Create mock data
        TrainingDetails trainingDetails = new TrainingDetails();
        trainingDetails.setTrainerName("pavs");
        trainingDetails.setTraineeName("sid");
        trainingDetails.setTrainingTypeId(1);

        User mockTrainerUser = new User();
        mockTrainerUser.setUsername("pavs");
        mockTrainerUser.setFirstName("Ammu");
        mockTrainerUser.setLastName("Pavani");
        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockTrainerUser);
        mockTrainerUser.setTrainer(mockTrainer);

        User mockTraineeUser = new User();
        mockTraineeUser.setUsername("sid");
        mockTraineeUser.setFirstName("Siddhu");
        mockTraineeUser.setLastName("Saai");
        Trainee mockTrainee = new Trainee();
        mockTrainee.setUser(mockTraineeUser);
        mockTraineeUser.setTrainee(mockTrainee);


        mockTrainee.setTrainerList(List.of(mockTrainer));
        mockTrainer.setTraineeList(List.of(mockTrainee));


        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);
        mockTrainingType.setTrainingTypeName("Zumba");

        mockTrainer.setTrainingType(new TrainingType());

        // Mock behavior
        Mockito.when(userService.getUser("pavs")).thenReturn(mockTrainerUser);
        Mockito.when(userService.getUser("sid")).thenReturn(mockTraineeUser);
        Mockito.when(trainingTypeService.get(1)).thenReturn(mockTrainingType);
//        Mockito.when(trainingRepository.save(Mockito.any(Training.class))).thenReturn(new Training());

        // Test
        assertThrows(TrainingException.class, () -> trainingService.add(trainingDetails));

    }


    @Test
    void testAddTrainingTrainingException2() throws UserException, TrainingTypeException, TrainerException, TraineeException, TrainingException {
        // Create mock data
        TrainingDetails trainingDetails = new TrainingDetails();
        trainingDetails.setTrainerName("pavs");
        trainingDetails.setTraineeName("sid");
        trainingDetails.setTrainingTypeId(1);

        User mockTrainerUser = new User();
        mockTrainerUser.setUsername("pavs");
        mockTrainerUser.setFirstName("Ammu");
        mockTrainerUser.setLastName("Pavani");
        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockTrainerUser);
        mockTrainerUser.setTrainer(mockTrainer);

        User mockTraineeUser = new User();
        mockTraineeUser.setUsername("sid");
        mockTraineeUser.setFirstName("Siddhu");
        mockTraineeUser.setLastName("Saai");
        Trainee mockTrainee = new Trainee();
        mockTrainee.setUser(mockTraineeUser);
        mockTraineeUser.setTrainee(mockTrainee);


        mockTrainee.setTrainerList(Collections.emptyList());
        mockTrainer.setTraineeList(Collections.emptyList());


        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);
        mockTrainingType.setTrainingTypeName("Zumba");

        mockTrainer.setTrainingType(new TrainingType());

        // Mock behavior
        Mockito.when(userService.getUser("pavs")).thenReturn(mockTrainerUser);
        Mockito.when(userService.getUser("sid")).thenReturn(mockTraineeUser);
        Mockito.when(trainingTypeService.get(1)).thenReturn(mockTrainingType);
//        Mockito.when(trainingRepository.save(Mockito.any(Training.class))).thenReturn(new Training());

        // Test
        assertThrows(TrainingException.class, () -> trainingService.add(trainingDetails));

    }

    @Test
    void testAddTrainingTraineeException() throws UserException, TrainingTypeException, TrainerException, TraineeException, TrainingException {
        // Create mock data
        TrainingDetails trainingDetails = new TrainingDetails();
        trainingDetails.setTrainerName("pavs");
        trainingDetails.setTraineeName("sid");
        trainingDetails.setTrainingTypeId(1);

        User mockTrainerUser = new User();
        mockTrainerUser.setUsername("pavs");
        mockTrainerUser.setFirstName("Ammu");
        mockTrainerUser.setLastName("Pavani");
        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockTrainerUser);
        mockTrainerUser.setTrainer(mockTrainer);

        User mockTraineeUser = new User();
        mockTraineeUser.setUsername("sid");
        mockTraineeUser.setFirstName("Siddhu");
        mockTraineeUser.setLastName("Saai");
        Trainee mockTrainee = new Trainee();
        mockTrainee.setUser(mockTraineeUser);


        mockTrainee.setTrainerList(Collections.emptyList());
        mockTrainer.setTraineeList(Collections.emptyList());


        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);
        mockTrainingType.setTrainingTypeName("Zumba");

        mockTrainer.setTrainingType(new TrainingType());

        // Mock behavior
        Mockito.when(userService.getUser("pavs")).thenReturn(mockTrainerUser);
        Mockito.when(userService.getUser("sid")).thenReturn(mockTraineeUser);
//        Mockito.when(trainingTypeService.get(1)).thenReturn(mockTrainingType);
//        Mockito.when(trainingRepository.save(Mockito.any(Training.class))).thenReturn(new Training());

        // Test
        assertThrows(TraineeException.class, () -> trainingService.add(trainingDetails));

    }

    @Test
    void testAddTrainingTrainerException() throws UserException, TrainingTypeException, TrainerException, TraineeException, TrainingException {
        // Create mock data
        TrainingDetails trainingDetails = new TrainingDetails();
        trainingDetails.setTrainerName("pavs");
        trainingDetails.setTraineeName("sid");
        trainingDetails.setTrainingTypeId(1);

        User mockTrainerUser = new User();
        mockTrainerUser.setUsername("pavs");
        mockTrainerUser.setFirstName("Ammu");
        mockTrainerUser.setLastName("Pavani");
        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockTrainerUser);


        User mockTraineeUser = new User();
        mockTraineeUser.setUsername("sid");
        mockTraineeUser.setFirstName("Siddhu");
        mockTraineeUser.setLastName("Saai");
        Trainee mockTrainee = new Trainee();
        mockTrainee.setUser(mockTraineeUser);
        mockTraineeUser.setTrainee(mockTrainee);


        mockTrainee.setTrainerList(Collections.emptyList());
        mockTrainer.setTraineeList(Collections.emptyList());


        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);
        mockTrainingType.setTrainingTypeName("Zumba");

        mockTrainer.setTrainingType(new TrainingType());

        // Mock behavior
        Mockito.when(userService.getUser("pavs")).thenReturn(mockTrainerUser);
//        Mockito.when(userService.getUser("sid")).thenReturn(mockTraineeUser);
//        Mockito.when(trainingTypeService.get(1)).thenReturn(mockTrainingType);
//        Mockito.when(trainingRepository.save(Mockito.any(Training.class))).thenReturn(new Training());

        // Test
        assertThrows(TrainerException.class, () -> trainingService.add(trainingDetails));

    }


    @Test
    void testGetTrainingsBetweenForTrainee() throws TrainingTypeException {
        // Create mock data
        Trainee mockTrainee = new Trainee();
        mockTrainee.setId(1);

        // Mock behavior
        Mockito.when(trainingTypeService.get(Mockito.anyInt())).thenReturn(new TrainingType());
        Mockito.when(trainingRepository.findAllTrainingInBetween(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(Trainee.class), Mockito.any(TrainingType.class))).thenReturn(new ArrayList<>());

        // Test
        List<Training> result = trainingService.getTrainingsBetweenForTrainee(mockTrainee, LocalDate.now(), LocalDate.now(), 1);

        // Verification
        assertNotNull(result);

        // Verify mock interactions
        Mockito.verify(trainingTypeService, Mockito.times(1)).get(Mockito.anyInt());
        Mockito.verify(trainingRepository, Mockito.times(1)).findAllTrainingInBetween(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(Trainee.class), Mockito.any(TrainingType.class));
    }

    @Test
    void testGetTrainingsBetweenForTraineeTrainingTypeException() throws TrainingTypeException {
        // Create mock data
        Trainee mockTrainee = new Trainee();
        mockTrainee.setId(1);

        // Mock behavior
        Mockito.when(trainingTypeService.get(Mockito.anyInt())).thenThrow(TrainingTypeException.class);

        // Test
        assertThrows(TrainingTypeException.class, () -> trainingService.getTrainingsBetweenForTrainee(mockTrainee, LocalDate.now(), LocalDate.now(), 1));


        // Verify mock interactions
        Mockito.verify(trainingTypeService, Mockito.times(1)).get(Mockito.anyInt());

    }

    @Test
    void testGetTrainingsBetweenForTrainer() throws UserException, TraineeException {
        // Create mock data
        User mockTrainerUser = new User();
        mockTrainerUser.setUsername("pavs");
        mockTrainerUser.setFirstName("Ammu");
        mockTrainerUser.setLastName("Pavani");
        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockTrainerUser);


        User mockTraineeUser = new User();
        mockTraineeUser.setUsername("sid");
        mockTraineeUser.setFirstName("Siddhu");
        mockTraineeUser.setLastName("Saai");
        Trainee mockTrainee = new Trainee();
        mockTrainee.setUser(mockTraineeUser);
        mockTraineeUser.setTrainee(mockTrainee);


        mockTrainee.setTrainerList(List.of(mockTrainer));
        mockTrainer.setTraineeList(List.of(mockTrainee));

        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(mockTraineeUser);
        Mockito.when(trainingRepository.findAllTrainingInBetween(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(Trainer.class), Mockito.any(Trainee.class))).thenReturn(new ArrayList<>());

        // Test
        List<Training> result = trainingService.getTrainingsBetweenForTrainer(mockTrainer, LocalDate.now(), LocalDate.now(), "sid");

        // Verification
        assertNotNull(result);

        // Verify mock interactions
        Mockito.verify(userService, Mockito.times(1)).getUser(Mockito.anyString());
        Mockito.verify(trainingRepository, Mockito.times(1)).findAllTrainingInBetween(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(Trainer.class), Mockito.any(Trainee.class));
    }

    @Test
    void testGetTrainingsBetweenForTrainerTraineeException() throws UserException, TraineeException {
        // Create mock data
        Trainer mockTrainer = new Trainer();
        mockTrainer.setId(2);

        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(new User());
//        Mockito.when(trainingRepository.findAllTrainingInBetween(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(Trainer.class), Mockito.any(Trainee.class))).thenReturn(new ArrayList<>());

        // Test
        assertThrows(TraineeException.class, () -> trainingService.getTrainingsBetweenForTrainer(mockTrainer, LocalDate.now(), LocalDate.now(), "sid"));

    }


}
