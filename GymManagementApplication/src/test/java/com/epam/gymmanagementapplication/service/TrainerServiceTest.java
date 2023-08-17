package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.UserDTO;
import com.epam.gymmanagementapplication.dto.request.TrainerDetails;
import com.epam.gymmanagementapplication.dto.request.TrainerProfileUpdate;
import com.epam.gymmanagementapplication.dto.request.TrainerTrainings;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.TraineeException;
import com.epam.gymmanagementapplication.exception.TrainerException;
import com.epam.gymmanagementapplication.exception.TrainingTypeException;
import com.epam.gymmanagementapplication.exception.UserException;
import com.epam.gymmanagementapplication.model.*;
import com.epam.gymmanagementapplication.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    TrainerRepository trainerRepository;
    @Mock
    TrainingTypeServiceImpl trainingTypeService;
    @Mock
    UserServiceImpl userService;
    @Mock
    TrainingServiceImpl trainingService;


    @InjectMocks
    TrainerServiceImpl trainerService;


    @Test
    void testAddTrainer() throws TrainingTypeException, UserException {
        // Create mock data
        TrainerDetails trainerDetails = new TrainerDetails();
        trainerDetails.setSpecializationId(1);
        trainerDetails.setFirstName("John");
        trainerDetails.setLastName("Doe");
        trainerDetails.setEMail("john@example.com");

        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setFirstName("John");
        mockUserDTO.setLastName("Doe");
        mockUserDTO.setEMail("john@example.com");

        User mockUser = new User();
        mockUser.setUsername("john123");

        // Mock behavior
        Mockito.when(trainingTypeService.get(Mockito.anyInt())).thenReturn(mockTrainingType);
        Mockito.when(userService.saveUser(Mockito.any())).thenReturn(mockUser);

        // Test
        Credential result = trainerService.addTrainer(trainerDetails);

        // Verification
        assertNotNull(result);
        assertEquals("john123", result.getUsername());
    }


    @Test
    void testAddTrainerUserException() throws TrainingTypeException, UserException {
        // Create mock data
        TrainerDetails trainerDetails = new TrainerDetails();
        trainerDetails.setSpecializationId(1);
        trainerDetails.setFirstName("John");
        trainerDetails.setLastName("Doe");
        trainerDetails.setEMail("john@example.com");

        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setFirstName("John");
        mockUserDTO.setLastName("Doe");
        mockUserDTO.setEMail("john@example.com");

        User mockUser = new User();
        mockUser.setUsername("john123");

        // Mock behavior
        Mockito.when(trainingTypeService.get(Mockito.anyInt())).thenReturn(mockTrainingType);
        Mockito.when(userService.saveUser(Mockito.any())).thenThrow(UserException.class);

        assertThrows(UserException.class, () -> trainerService.addTrainer(trainerDetails));


    }

    @Test
    void testAddTrainerTrainingException() throws TrainingTypeException, UserException {
        // Create mock data
        TrainerDetails trainerDetails = new TrainerDetails();
        trainerDetails.setSpecializationId(1);
        trainerDetails.setFirstName("John");
        trainerDetails.setLastName("Doe");
        trainerDetails.setEMail("john@example.com");

        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setFirstName("John");
        mockUserDTO.setLastName("Doe");
        mockUserDTO.setEMail("john@example.com");

        User mockUser = new User();
        mockUser.setUsername("john123");

        // Mock behavior
        Mockito.when(trainingTypeService.get(Mockito.anyInt())).thenThrow(TrainingTypeException.class);


        assertThrows(TrainingTypeException.class, () -> trainerService.addTrainer(trainerDetails));


    }

    @Test
    void testGetTrainerProfile() throws UserException, TrainerException {

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Zumba");
        trainingType.setId(1);

        User traineeUser = new User(1, "Siddhu", "saai", "sid", "1234", true, "sid@gmail.com", LocalDate.now(), null, null);
        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Guntur");
        traineeUser.setTrainee(trainee);
        trainee.setUser(traineeUser);


        User trainerUser = new User(2, "Ammu", "Pavani", "pavs", "1234", true, "pavs@gmail.com", LocalDate.now(), null, null);
        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(trainerUser);
        trainerUser.setTrainer(mockTrainer);
        mockTrainer.setTrainingType(trainingType);

        mockTrainer.setTraineeList(List.of(trainee));
        trainee.setTrainerList(List.of(mockTrainer));


        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(trainerUser);

        // Test
        TrainerProfile result = trainerService.getTrainerProfile(mockTrainer.getUser().getUsername());

        // Verification
        assertNotNull(result);

    }

    @Test
    void testGetTrainerProfileTrainerException() throws UserException {
        // Create mock data
        String trainerUsername = "john123";

        User mockUser = new User();
        mockUser.setUsername(trainerUsername);

        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockUser);

        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(mockUser);

        // Test
        assertThrows(TrainerException.class, () -> trainerService.getTrainerProfile(trainerUsername));

    }


    @Test
    void testUpdateTrainer() throws UserException, TrainerException, TrainingTypeException {
        // Create mock data
        TrainerProfileUpdate trainerProfileUpdate = new TrainerProfileUpdate();
        trainerProfileUpdate.setUsername("pavs");
        trainerProfileUpdate.setFirstName("Updated First Name");
        trainerProfileUpdate.setLastName("Updated Last Name");
        trainerProfileUpdate.setEMail("updated@example.com");
        trainerProfileUpdate.setSpecializationId(1);
        trainerProfileUpdate.setActive(true);

        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);

        User mockUser = new User();
        mockUser.setUsername("pavs");
        mockUser.setFirstName("Ammu");
        mockUser.setLastName("Pavani");
        mockUser.setEMail("pavs@gmail.com");

        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockUser);

        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(mockUser);
        Mockito.when(trainingTypeService.get(Mockito.anyInt())).thenReturn(mockTrainingType);
        Mockito.when(trainerRepository.findByUser(Mockito.any())).thenReturn(Optional.of(mockTrainer));

        // Test
        TrainerProfile result = trainerService.updateTrainer(trainerProfileUpdate);

        // Verification
        assertNotNull(result);
        assertEquals("Updated First Name", result.getFirstName());
        assertEquals("Updated Last Name", result.getLastName());
        assertEquals("updated@example.com", result.getEMail());
        assertTrue(result.isActive());

        // Verify mock interactions
        Mockito.verify(userService).getUser("pavs");
        Mockito.verify(trainingTypeService).get(1);
        Mockito.verify(trainerRepository).findByUser(mockUser);
    }

    @Test
    void testUpdateTrainerUserException() throws UserException, TrainerException, TrainingTypeException {
        // Create mock data
        TrainerProfileUpdate trainerProfileUpdate = new TrainerProfileUpdate();
        trainerProfileUpdate.setUsername("pavs");
        trainerProfileUpdate.setFirstName("Updated First Name");
        trainerProfileUpdate.setLastName("Updated Last Name");
        trainerProfileUpdate.setEMail("updated@example.com");
        trainerProfileUpdate.setSpecializationId(1);
        trainerProfileUpdate.setActive(true);

        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);

        User mockUser = new User();
        mockUser.setUsername("pavs");
        mockUser.setFirstName("Ammu");
        mockUser.setLastName("Pavani");
        mockUser.setEMail("pavs@gmail.com");

        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockUser);

        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenThrow(UserException.class);


        // Test
        assertThrows(UserException.class, () -> trainerService.updateTrainer(trainerProfileUpdate));

    }

    @Test
    void testUpdateTrainerTrainingTypeException() throws UserException, TrainerException, TrainingTypeException {
        // Create mock data
        TrainerProfileUpdate trainerProfileUpdate = new TrainerProfileUpdate();
        trainerProfileUpdate.setUsername("pavs");
        trainerProfileUpdate.setFirstName("Updated First Name");
        trainerProfileUpdate.setLastName("Updated Last Name");
        trainerProfileUpdate.setEMail("updated@example.com");
        trainerProfileUpdate.setSpecializationId(1);
        trainerProfileUpdate.setActive(true);

        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);

        User mockUser = new User();
        mockUser.setUsername("pavs");
        mockUser.setFirstName("Ammu");
        mockUser.setLastName("Pavani");
        mockUser.setEMail("pavs@gmail.com");

        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockUser);

        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(mockUser);
        Mockito.when(trainingTypeService.get(Mockito.anyInt())).thenThrow(TrainingTypeException.class);


        assertThrows(TrainingTypeException.class, () -> trainerService.updateTrainer(trainerProfileUpdate));
    }


    @Test
    void testUpdateTrainerTrainerException() throws UserException, TrainerException, TrainingTypeException {
        // Create mock data
        TrainerProfileUpdate trainerProfileUpdate = new TrainerProfileUpdate();
        trainerProfileUpdate.setUsername("pavs");
        trainerProfileUpdate.setFirstName("Updated First Name");
        trainerProfileUpdate.setLastName("Updated Last Name");
        trainerProfileUpdate.setEMail("updated@example.com");
        trainerProfileUpdate.setSpecializationId(1);
        trainerProfileUpdate.setActive(true);

        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setId(1);

        User mockUser = new User();
        mockUser.setUsername("pavs");
        mockUser.setFirstName("Ammu");
        mockUser.setLastName("Pavani");
        mockUser.setEMail("pavs@gmail.com");

        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(mockUser);

        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(mockUser);
        Mockito.when(trainingTypeService.get(Mockito.anyInt())).thenReturn(mockTrainingType);
        Mockito.when(trainerRepository.findByUser(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(TrainerException.class, () -> trainerService.updateTrainer(trainerProfileUpdate));


    }


    @Test
    void testGetTrainings() throws UserException, TraineeException {
        // Create mock data
        TrainerTrainings trainerTrainings = new TrainerTrainings("pavs", LocalDate.now().minusDays(30), LocalDate.now(), "Siddhu");
        trainerTrainings.setUsername("pavs");
        trainerTrainings.setFromDate(LocalDate.now().minusDays(30));
        trainerTrainings.setEndDate(LocalDate.now());
        trainerTrainings.setTraineeName("Siddhu");

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Zumba");
        trainingType.setId(1);

        User traineeUser = new User(1, "Siddhu", "saai", "sid", "1234", true, "sid@gmail.com", LocalDate.now(), null, null);
        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Guntur");
        traineeUser.setTrainee(trainee);
        trainee.setUser(traineeUser);


        User trainerUser = new User(2, "Ammu", "Pavani", "pavs", "1234", true, "pavs@gmail.com", LocalDate.now(), null, null);
        Trainer mockTrainer = new Trainer();
        mockTrainer.setUser(trainerUser);
        trainerUser.setTrainer(mockTrainer);
        mockTrainer.setTrainingType(trainingType);

        mockTrainer.setTraineeList(List.of(trainee));
        trainee.setTrainerList(List.of(mockTrainer));

        List<Training> mockTrainingList = new ArrayList<>();
        Training mockTraining = new Training();
        mockTraining.setTrainingName("Zumba");
        mockTraining.setTrainingDate(LocalDate.now().minusDays(15));
        mockTraining.setTrainingType(new TrainingType());
        mockTraining.setTrainingDuration(60L);
        mockTraining.setTrainer(mockTrainer);
        mockTraining.setTrainee(trainee);
        mockTrainingList.add(mockTraining);

        // Mock behavior
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(trainerUser);
        Mockito.when(trainingService.getTrainingsBetweenForTrainer(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mockTrainingList);

        // Test
        List<TrainingDetails> result = trainerService.getTrainings(trainerTrainings);

        // Verification
        assertNotNull(result);
        assertEquals(1, result.size());
        TrainingDetails trainingDetails = result.get(0);
        assertEquals("Zumba", trainingDetails.getName());
        assertEquals(LocalDate.now().minusDays(15), trainingDetails.getDate());
        assertEquals(60, trainingDetails.getDuration());
        assertEquals("pavs", trainingDetails.getTrainerName());
        assertEquals("sid", trainingDetails.getTraineeName());

        // Verify mock interactions
        Mockito.verify(userService).getUser("pavs");
        Mockito.verify(trainingService).getTrainingsBetweenForTrainer(mockTrainer, trainerTrainings.getFromDate(), trainerTrainings.getEndDate(), trainerTrainings.getTraineeName());
    }


    @Test
    void testFetchAllTrainers() {
        // Create mock data
        User mockUser1 = new User();
        mockUser1.setUsername("pavs");
        mockUser1.setFirstName("Ammu");
        mockUser1.setLastName("Pavani");
        mockUser1.setEMail("pavs@gmail.com");

        User mockUser2 = new User();
        mockUser2.setUsername("john");
        mockUser2.setFirstName("John");
        mockUser2.setLastName("Doe");
        mockUser2.setEMail("john@example.com");

        Trainer mockTrainer1 = new Trainer();
        mockTrainer1.setUser(mockUser1);

        Trainer mockTrainer2 = new Trainer();
        mockTrainer2.setUser(mockUser2);

        List<Trainer> mockTrainerList = new ArrayList<>();
        mockTrainerList.add(mockTrainer1);
        mockTrainerList.add(mockTrainer2);

        // Mock behavior
        Mockito.when(trainerRepository.findAll()).thenReturn(mockTrainerList);

        // Test
        List<Trainer> result = trainerService.fetchAllTrainers();

        // Verification
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify mock interactions
        Mockito.verify(trainerRepository).findAll();
    }


}
