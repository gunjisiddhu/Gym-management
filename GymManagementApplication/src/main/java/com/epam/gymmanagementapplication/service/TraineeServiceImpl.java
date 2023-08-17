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
import com.epam.gymmanagementapplication.model.Trainee;
import com.epam.gymmanagementapplication.model.Trainer;
import com.epam.gymmanagementapplication.model.Training;
import com.epam.gymmanagementapplication.model.User;
import com.epam.gymmanagementapplication.repository.TraineeRepository;
import com.epam.gymmanagementapplication.util.CredentialGenerator;
import com.epam.gymmanagementapplication.util.StringConstants;
import com.epam.gymmanagementapplication.util.ValueMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.gymmanagementapplication.service.TrainerServiceImpl.getTrainingDetails;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final UserServiceImpl userService;
    private final TrainerServiceImpl trainerService;
    private final TrainingServiceImpl trainingService;


    public Credential addNewTrainee(TraineeDetails traineeDetails) throws UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), ADD_NEW_TRAINEE, this.getClass().getName(), traineeDetails.toString());
        UserDTO userDTO = UserDTO.builder()
                .firstName(traineeDetails.getFirstName())
                .lastName(traineeDetails.getLastName())
                .eMail(traineeDetails.getEMail())
                .username(CredentialGenerator.generateUsername(traineeDetails.getFirstName(), traineeDetails.getLastName()))
                .password(CredentialGenerator.generateRandomPassword(9))
                .build();

        User user = userService.saveUser(userDTO);
        Trainee trainee = new Trainee();
        Optional.ofNullable(traineeDetails.getDateOfBirth()).ifPresent(trainee::setDateOfBirth);
        Optional.ofNullable(traineeDetails.getAddress()).ifPresent(trainee::setAddress);
        trainee.setUser(user);
        traineeRepository.save(trainee);
        Credential credential = new Credential(user.getUsername(), userDTO.getPassword());
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), ADD_NEW_TRAINEE, this.getClass().getName());
        return credential;

    }


    public TraineeProfile getTraineeProfile(String username) throws UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_TRAINEE_PROFILE, this.getClass().getName(), username);
        User user = userService.getUser(username);
        Trainee trainee = user.getTrainee();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_TRAINEE_PROFILE, this.getClass().getName());
        return ValueMapper.createTraineeProfileFromUserAndTrainee(user, trainee);
    }

    @Transactional
    public TraineeProfile updateTrainee(TraineeProfileUpdate traineeProfileUpdate) throws UserException, TraineeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), UPDATE_TRAINEE, this.getClass().getName(), traineeProfileUpdate.toString());
        User user = userService.getUser(traineeProfileUpdate.getUsername());
        Trainee updatedTrainee = traineeRepository.findByUser(user).map(trainee -> {
            trainee.getUser().setFirstName(traineeProfileUpdate.getFirstName());
            trainee.getUser().setLastName(traineeProfileUpdate.getLastName());
            trainee.getUser().setEMail(traineeProfileUpdate.getEMail());
            trainee.setDateOfBirth(traineeProfileUpdate.getDateOfBirth());
            trainee.setAddress(traineeProfileUpdate.getAddress());
            trainee.getUser().setActive(traineeProfileUpdate.isActive());
            return trainee;
        }).orElseThrow(() -> new TraineeException("Not found the trainee"));
        TraineeProfile traineeProfile = ValueMapper.createTraineeProfileFromUserAndTrainee(user, updatedTrainee);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), UPDATE_TRAINEE, this.getClass().getName());
        return traineeProfile;
    }


    @Transactional
    public void removeTrainee(String username) throws UserException, TraineeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), REMOVE_TRAINEE, this.getClass().getName(), username);
        User user = userService.getUser(username);
        Trainee trainee = Optional.ofNullable(user.getTrainee()).orElseThrow(() -> new TraineeException("No trainee found"));
        trainee.getTrainerList().forEach(trainer -> trainer.getTraineeList().remove(trainee));
        userService.removeUser(user);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), REMOVE_TRAINEE, this.getClass().getName());
    }

    @Transactional
    public List<TrainerProfile> updateTrainersForTrainee(TraineeTrainersList trainersList) throws UserException, TrainerException, TraineeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), UPDATE_TRAINERS_FOR_TRAINEE, this.getClass().getName(), trainersList.toString());
        List<Trainer> trainers = new ArrayList<>();
        for (String user : trainersList.getTrainerUsernameList()) {
            Trainer trainer = Optional.ofNullable(userService.getUser(user).getTrainer()).orElseThrow(() -> new TrainerException("Trainer Not Found"));
            trainers.add(trainer);
        }
        Trainee trainee = Optional.ofNullable(userService.getUser(trainersList.getTraineeUsername()).getTrainee()).orElseThrow(() -> new TraineeException("Trainee not found"));
        trainee.setTrainerList(trainers);
        List<TrainerProfile> trainerProfileList = new ArrayList<>();
        for (String trainer : trainersList.getTrainerUsernameList()) {
            trainerProfileList.add(trainerService.getTrainerProfile(trainer));
        }
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), UPDATE_TRAINERS_FOR_TRAINEE, this.getClass().getName());
        return trainerProfileList;
    }

    public List<TrainerProfile> getOtherTrainersOtherThanCurrentTrainee(String username) throws UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_OTHER_ACTIVE_TRAINERS, this.getClass().getName(), username);
        User user = userService.getUser(username);
        List<Trainer> trainersList;
        if (!user.getTrainee().getTrainerList().isEmpty())
            trainersList = traineeRepository.findTrainersNotInList(user.getTrainee().getTrainerList());
        else
            trainersList = trainerService.fetchAllTrainers();
        List<TrainerProfile> trainerProfileList = new ArrayList<>();
        for (Trainer trainer : trainersList) {
            if (trainer.getUser().isActive())
                trainerProfileList.add(ValueMapper.createTrainerProfileFromUserAndTrainer(trainer.getUser(), trainer));
        }
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_OTHER_ACTIVE_TRAINERS, this.getClass().getName());
        return trainerProfileList;
    }


    public List<TrainingDetails> getTrainings(TraineeTrainings traineeTrainings) throws UserException, TraineeException, TrainingTypeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_TRAININGS, this.getClass().getName(), traineeTrainings.toString());
        Trainee trainee = Optional.ofNullable(userService.getUser(traineeTrainings.getUsername()).getTrainee()).orElseThrow(() -> new TraineeException("Trainee not found"));
        List<Training> trainingList = trainingService.getTrainingsBetweenForTrainee(trainee, traineeTrainings.getFromDate(), traineeTrainings.getEndDate(), traineeTrainings.getTrainingTypeId());
        List<TrainingDetails> trainingDetails = getTrainingDetails(trainingList);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_TRAININGS, this.getClass().getName());
        return trainingDetails;
    }
}
