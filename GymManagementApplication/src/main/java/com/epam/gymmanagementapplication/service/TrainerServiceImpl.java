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
import com.epam.gymmanagementapplication.model.Trainer;
import com.epam.gymmanagementapplication.model.Training;
import com.epam.gymmanagementapplication.model.TrainingType;
import com.epam.gymmanagementapplication.model.User;
import com.epam.gymmanagementapplication.repository.TrainerRepository;
import com.epam.gymmanagementapplication.util.CredentialGenerator;
import com.epam.gymmanagementapplication.util.StringConstants;
import com.epam.gymmanagementapplication.util.ValueMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeServiceImpl trainingTypeService;
    private final UserServiceImpl userService;
    private final TrainingServiceImpl trainingService;

    @NotNull
    static List<TrainingDetails> getTrainingDetails(List<Training> trainingList) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_TRAINING_DETAILS, TrainerServiceImpl.class.getName(), trainingList.toString());
        List<TrainingDetails> trainingDetails = new ArrayList<>();
        trainingList.forEach(trainingDetail -> trainingDetails.add(new TrainingDetails(
                trainingDetail.getTrainingName(),
                trainingDetail.getTrainingDate(),
                trainingDetail.getTrainingType().getId(),
                trainingDetail.getTrainingDuration(),
                trainingDetail.getTrainer().getUser().getUsername(),
                trainingDetail.getTrainee().getUser().getUsername()
        )));
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_TRAINING_DETAILS, TrainerServiceImpl.class.getName());
        return trainingDetails;
    }

    public Credential addTrainer(TrainerDetails trainerDetails) throws TrainingTypeException, UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), ADD_TRAINER, this.getClass().getName(), trainerDetails.toString());
        TrainingType trainingType = trainingTypeService.get(trainerDetails.getSpecializationId());
        UserDTO userDTO = UserDTO.builder()
                .firstName(trainerDetails.getFirstName())
                .lastName(trainerDetails.getLastName())
                .eMail(trainerDetails.getEMail())
                .username(CredentialGenerator.generateUsername(trainerDetails.getFirstName(), trainerDetails.getLastName()))
                .password(CredentialGenerator.generateRandomPassword(9))
                .build();

        User user = userService.saveUser(userDTO);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        trainerRepository.save(trainer);
        Credential credential = new Credential(user.getUsername(), userDTO.getPassword());
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), ADD_TRAINER, this.getClass().getName());
        return credential;
    }

    public TrainerProfile getTrainerProfile(String username) throws UserException, TrainerException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_TRAINER_PROFILE, this.getClass().getName(), username);
        User user = userService.getUser(username);
        Trainer trainer = Optional.ofNullable(user.getTrainer()).orElseThrow(() -> new TrainerException("No Trainer Found!!"));
        TrainerProfile trainerProfile = ValueMapper.createTrainerProfileFromUserAndTrainer(user, trainer);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_TRAINER_PROFILE, this.getClass().getName());
        return trainerProfile;
    }

    @Transactional
    public TrainerProfile updateTrainer(TrainerProfileUpdate trainerProfileUpdate) throws UserException, TrainerException, TrainingTypeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), UPDATE_TRAINER, this.getClass().getName(), trainerProfileUpdate.toString());
        User user = userService.getUser(trainerProfileUpdate.getUsername());
        TrainingType trainingType = trainingTypeService.get(trainerProfileUpdate.getSpecializationId());
        Trainer updatedTrainer = trainerRepository.findByUser(user).map(trainer -> {
            trainer.getUser().setFirstName(trainerProfileUpdate.getFirstName());
            trainer.getUser().setLastName(trainerProfileUpdate.getLastName());
            trainer.getUser().setActive(trainerProfileUpdate.isActive());
            trainer.getUser().setEMail(trainerProfileUpdate.getEMail());
            trainer.setTrainingType(trainingType);
            return trainer;
        }).orElseThrow(() -> new TrainerException("Not found the trainee"));
        TrainerProfile trainerProfile = ValueMapper.createTrainerProfileFromUserAndTrainer(user, updatedTrainer);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), UPDATE_TRAINER, this.getClass().getName());
        return trainerProfile;
    }

    public List<TrainingDetails> getTrainings(TrainerTrainings trainerTrainings) throws UserException, TraineeException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_TRAININGS, this.getClass().getName(), trainerTrainings.toString());
        Trainer trainer = Optional.ofNullable(userService.getUser(trainerTrainings.getUsername()).getTrainer()).orElseThrow(() -> new TraineeException("Trainee not found"));
        List<Training> trainingList = trainingService.getTrainingsBetweenForTrainer(trainer, trainerTrainings.getFromDate(), trainerTrainings.getEndDate(), trainerTrainings.getTraineeName());
        List<TrainingDetails> trainingDetails = getTrainingDetails(trainingList);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_TRAININGS, this.getClass().getName());
        return trainingDetails;
    }

    @Override
    public List<Trainer> fetchAllTrainers() {
        return trainerRepository.findAll();
    }
}
