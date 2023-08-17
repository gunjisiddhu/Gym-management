package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.UserDTO;
import com.epam.gymmanagementapplication.dto.request.TraineeDetails;
import com.epam.gymmanagementapplication.dto.request.TraineeProfileUpdate;
import com.epam.gymmanagementapplication.dto.request.TraineeTrainersList;
import com.epam.gymmanagementapplication.dto.request.TraineeTrainings;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TraineeProfile;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.TraineeException;
import com.epam.gymmanagementapplication.exception.TrainerException;
import com.epam.gymmanagementapplication.exception.TrainingTypeException;
import com.epam.gymmanagementapplication.exception.UserException;
import com.epam.gymmanagementapplication.model.*;
import com.epam.gymmanagementapplication.repository.TraineeRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainingServiceImpl trainingService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @NotNull
    private static Trainer getTrainer(String trainerUsername1, String trainerUsername2) {
        User trainerUser1 = new User();
        trainerUser1.setUsername(trainerUsername1);
        Trainer trainer1 = new Trainer();
        trainer1.setUser(trainerUser1);
        trainerUser1.setTrainer(trainer1);

        User trainerUser2 = new User();
        trainerUser2.setActive(true);
        trainerUser2.setUsername(trainerUsername2);
        Trainer trainer2 = new Trainer();
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1);
        trainingType.setTrainingTypeName("Zumba");
        trainer2.setTrainingType(trainingType);
        trainer2.setUser(trainerUser2);
        trainerUser2.setTrainer(trainer2);
        return trainer2;
    }

    @Test
    void testAddNewTrainee() throws UserException {
        TraineeDetails traineeDetails = new TraineeDetails("John", "Doe", "john@example.com", LocalDate.now(), "Address");
        User user = new User();
        user.setUsername("john123");
        user.setTrainee(new Trainee());
        Mockito.when(userService.saveUser(any(UserDTO.class))).thenReturn(user);
        Credential credential = traineeService.addNewTrainee(traineeDetails);
        assertNotNull(credential);
        assertEquals("john123", credential.getUsername());
        assertNotNull(credential.getPassword());
    }

    @Test
    void testAddNewTraineeExceptionCase() throws UserException {
        TraineeDetails traineeDetails = new TraineeDetails("John", "Doe", "john@example.com", LocalDate.now(), "Address");
        User user = new User();
        user.setUsername("john123");
        user.setTrainee(new Trainee());
        Mockito.when(userService.saveUser(any(UserDTO.class))).thenThrow(new UserException("User Exception"));
        assertThrows(UserException.class, () -> traineeService.addNewTrainee(traineeDetails));
    }

    @Test
    void testGetTraineeProfile() throws UserException {
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

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1);
        trainingType.setTrainingTypeName("Zumba");

        mockTrainer.setTrainingType(trainingType);

        mockTrainer.setTraineeList(List.of(trainee));
        trainee.setTrainerList(List.of(mockTrainer));


        Mockito.when(userService.getUser(any(String.class))).thenReturn(traineeUser);
        TraineeProfile traineeProfile = traineeService.getTraineeProfile(traineeUser.getUsername());
        assertNotNull(traineeProfile);
        assertEquals(LocalDate.now(), traineeProfile.getDateOfBirth());
        assertEquals("Guntur", traineeProfile.getAddress());
    }

    @Test
    void testGetTraineeProfileExceptionCase() throws UserException {
        String username = "john123";
        User user = new User();
        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Address");
        trainee.setTrainerList(Collections.emptyList());
        user.setTrainee(trainee);
        Mockito.when(userService.getUser(username)).thenThrow(UserException.class);
        assertThrows(UserException.class, () -> traineeService.getTraineeProfile(username));
    }

    @Test
    void testUpdateTrainee() throws UserException, TraineeException {
        // Create mock data
        String username = "john123";
        TraineeProfileUpdate traineeProfileUpdate = new TraineeProfileUpdate(username, "John", "Doe", "john@example.com", LocalDate.now(), "New Address", true);
        User user = new User();
        user.setUsername(username);
        user.setPassword("123");
        user.setFirstName("Siddhu");
        user.setLastName("Saai");
        user.setEMail("sid@gmail.com");
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Address");
        trainee.setTrainerList(Collections.emptyList());
        user.setTrainee(trainee);
        trainee.setUser(user);

        // Mock behavior
        Mockito.when(userService.getUser(username)).thenReturn(user);
        Mockito.when(traineeRepository.findByUser(user)).thenReturn(Optional.of(trainee));

        // Test
        TraineeProfile traineeProfile = traineeService.updateTrainee(traineeProfileUpdate);

        // Assertion
        assertNotNull(traineeProfile);
        assertEquals("John", traineeProfile.getFirstName());
        assertEquals("Doe", traineeProfile.getLastName());
        assertEquals("john@example.com", traineeProfile.getEMail());
        assertEquals(LocalDate.now(), traineeProfile.getDateOfBirth());
        assertEquals("New Address", traineeProfile.getAddress());
        assertTrue(traineeProfile.isActive());
    }

    @Test
    void testUpdateTraineeTraineeNotFound() throws UserException {
        // Create mock data
        String username = "john123";
        TraineeProfileUpdate traineeProfileUpdate = new TraineeProfileUpdate(username, "John", "Doe", "john@example.com", LocalDate.now(), "New Address", true);
        User user = new User();
        user.setUsername(username);

        // Mock behavior
        Mockito.when(userService.getUser(username)).thenReturn(user);
        Mockito.when(traineeRepository.findByUser(user)).thenReturn(Optional.empty());

        // Test
        assertThrows(TraineeException.class, () -> traineeService.updateTrainee(traineeProfileUpdate)); // This should throw TraineeException
    }

    @Test
    void testUpdateTraineeUserNotFound() throws UserException {
        // Create mock data
        String username = "john123";
        TraineeProfileUpdate traineeProfileUpdate = new TraineeProfileUpdate(username, "John", "Doe", "john@example.com", LocalDate.now(), "New Address", true);
        User user = new User();
        user.setUsername(username);

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        // Mock behavior
        Mockito.when(userService.getUser(username)).thenThrow(UserException.class);

        // Test
        assertThrows(UserException.class, () -> traineeService.updateTrainee(traineeProfileUpdate)); // This should throw TraineeException
    }

    @Test
    void testRemoveTrainee() throws UserException, TraineeException {
        // Create mock data
        String username = "john123";
        User user = new User();
        user.setUsername(username);
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setTrainerList(Collections.emptyList());
        user.setTrainee(trainee);
        // Mock behavior
        Mockito.when(userService.getUser(username)).thenReturn(user);


        // Test
        traineeService.removeTrainee(username);

        // Verification
        Mockito.verify(userService).removeUser(user);
    }

    @Test
    void testRemoveTraineeTraineeNotFound() throws UserException {
        // Create mock data
        String username = "john123";
        User user = new User();
        user.setUsername(username);
        user.setTrainee(null);
        // Mock behavior
        Mockito.when(userService.getUser(username)).thenReturn(user);


        // Test
        assertThrows(TraineeException.class, () -> traineeService.removeTrainee(username)); // This should throw TraineeException
    }

    @Test
    void testUpdateTrainersForTrainee() throws UserException, TrainerException, TraineeException {
        // Create mock data
        String traineeUsername = "trainee123";
        String trainerUsername1 = "trainer1";
        String trainerUsername2 = "trainer2";
        TraineeTrainersList trainersList = new TraineeTrainersList();
        trainersList.setTraineeUsername(traineeUsername);
        trainersList.setTrainerUsernameList(Arrays.asList(trainerUsername1, trainerUsername2));

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        traineeUser.setTrainee(trainee);

        User trainerUser1 = new User();
        trainerUser1.setUsername(trainerUsername1);
        Trainer trainer1 = new Trainer();
        trainer1.setUser(trainerUser1);
        trainerUser1.setTrainer(trainer1);


        User trainerUser2 = new User();
        trainerUser2.setUsername(trainerUsername2);
        Trainer trainer2 = new Trainer();
        trainer2.setUser(trainerUser2);
        trainerUser2.setTrainer(trainer2);
        // Mock behavior
        Mockito.when(userService.getUser(traineeUsername)).thenReturn(traineeUser);
        Mockito.when(userService.getUser(trainerUsername1)).thenReturn(trainerUser1);
        Mockito.when(userService.getUser(trainerUsername2)).thenReturn(trainerUser2);
        Mockito.when(trainerService.getTrainerProfile(trainerUsername1)).thenReturn(new TrainerProfile());
        Mockito.when(trainerService.getTrainerProfile(trainerUsername2)).thenReturn(new TrainerProfile());

        // Test
        List<TrainerProfile> result = traineeService.updateTrainersForTrainee(trainersList);

        // Verification
        // Mockito.verify(traineeRepository).save(trainee);
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateTrainersForTraineeTrainerNotFound() throws UserException, TrainerException, TraineeException {
        // Create mock data
        String traineeUsername = "trainee123";
        String trainerUsername1 = "trainer1";
        String trainerUsername2 = "trainer2";
        TraineeTrainersList trainersList = new TraineeTrainersList();
        trainersList.setTraineeUsername(traineeUsername);
        trainersList.setTrainerUsernameList(Arrays.asList(trainerUsername1, trainerUsername2));

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        traineeUser.setTrainee(trainee);
        trainee.setTrainerList(Collections.emptyList());

        // Mock behavior
        Mockito.when(userService.getUser(any(String.class))).thenReturn(new User(1, "Siddhu", "saai", "sid", "1234", true, "sid@gmail.com", LocalDate.now(), null, null));
        //Mockito.when(userService.getUser(trainerUsername1)).thenReturn(null); // Simulate trainer not found

        // Test
        assertThrows(TrainerException.class, () -> traineeService.updateTrainersForTrainee(trainersList)); // This should throw TrainerException
    }

    @Test
    void testUpdateTrainersForTraineeTraineeNotFound() throws UserException, TrainerException, TraineeException {
        // Create mock data
        String traineeUsername = "trainee123";
        TraineeTrainersList trainersList = new TraineeTrainersList();
        trainersList.setTraineeUsername(traineeUsername);
        trainersList.setTrainerUsernameList(Collections.emptyList());

        // Mock behavior
        Mockito.when(userService.getUser(traineeUsername)).thenReturn(new User(1, "Siddhu", "saai", "sid", "1234", true, "sid@gmail.com", LocalDate.now(), null, null));

        // Test
        assertThrows(TraineeException.class, () -> traineeService.updateTrainersForTrainee(trainersList)); // This should throw TraineeException
    }

    @Test
    void testGetOtherTrainersOtherThanCurrentTrainee() throws UserException {
        // Create mock data
        String traineeUsername = "trainee123";
        String trainerUsername1 = "trainer1";
        String trainerUsername2 = "trainer2";

        User traineeUser = new User();
        traineeUser.setUsername(traineeUsername);
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainerList(Collections.emptyList());
        traineeUser.setTrainee(trainee);

        Trainer trainer2 = getTrainer(trainerUsername1, trainerUsername2);

        // Mock behavior
        Mockito.when(userService.getUser(traineeUsername)).thenReturn(traineeUser);
        Mockito.when(trainerService.fetchAllTrainers()).thenReturn(List.of(trainer2));

        assertDoesNotThrow(() -> traineeService.getOtherTrainersOtherThanCurrentTrainee(traineeUsername));
    }

    @Test
    void testGetOtherTrainersOtherThanCurrentTraineeEmptyList() throws UserException {
        // Create mock data
        String traineeUsername = "trainee123";

        User traineeUser = new User(1, "Siddhu", "saai", "sid", "1234", true, "sid@gmail.com", LocalDate.now(), null, null);
        traineeUser.setUsername(traineeUsername);
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainerList(Collections.singletonList(new Trainer()));
        traineeUser.setTrainee(trainee);


        // Mock behavior
        Mockito.when(userService.getUser(traineeUsername)).thenReturn(traineeUser);
        Mockito.when(traineeRepository.findTrainersNotInList(Mockito.any())).thenReturn(Collections.emptyList());

        // Test
        List<TrainerProfile> result = traineeService.getOtherTrainersOtherThanCurrentTrainee(traineeUsername);

        // Verification
        assertTrue(result.isEmpty());
    }


    @Test
    void testGetTrainings() throws UserException, TraineeException, TrainingTypeException {
        // Create mock data
        String traineeUsername = "trainee123";
        LocalDate fromDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        int trainingTypeId = 1;

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1);
        trainingType.setTrainingTypeName("Zumba");

        User traineeUser = new User(1, "Siddhu", "saai", "sid", "1234", true, "sid@gmail.com", LocalDate.now(), null, null);
        traineeUser.setUsername(traineeUsername);
        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        traineeUser.setTrainee(trainee);

        User trainerUser2 = new User();
        trainerUser2.setUsername("Sid");
        Trainer trainer2 = new Trainer();
        trainer2.setUser(trainerUser2);
        trainerUser2.setTrainer(trainer2);


        List<Training> mockTrainingList = new ArrayList<>();
        Training mockTraining = new Training();
        mockTraining.setTrainingName("Mock Training");
        mockTraining.setTrainingDate(LocalDate.now());
        mockTraining.setId(trainingTypeId);
        mockTraining.setTrainingDuration(60L);
        mockTraining.setTrainer(trainer2);
        mockTraining.setTrainee(trainee);
        mockTraining.setTrainingType(trainingType);
        mockTrainingList.add(mockTraining);

        // Mock behavior
        Mockito.when(userService.getUser(any())).thenReturn(traineeUser);
        Mockito.when(trainingService.getTrainingsBetweenForTrainee(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt())).thenReturn(mockTrainingList);

        // Test
        List<TrainingDetails> result = traineeService.getTrainings(new TraineeTrainings(traineeUsername, fromDate, endDate, "Siddhu", 1));

        // Verification
        assertEquals(1, result.size());
    }


}
