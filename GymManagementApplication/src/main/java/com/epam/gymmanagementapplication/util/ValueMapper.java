package com.epam.gymmanagementapplication.util;

import com.epam.gymmanagementapplication.dto.UserDTO;
import com.epam.gymmanagementapplication.dto.request.NotificationDTO;
import com.epam.gymmanagementapplication.dto.request.TrainingReportDTO;
import com.epam.gymmanagementapplication.dto.response.*;
import com.epam.gymmanagementapplication.exception.UserException;
import com.epam.gymmanagementapplication.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ValueMapper {


    private static final String USER_DTO_TO_USER_MAPPER = "userDtoToUserMapper";
    private static final String CREATE_TRAINEE_PROFILE_FROM_USER_AND_TRAINEE = "createTraineeProfileFromUserAndTrainee";
    private static final String CREATE_TRAINER_PROFILE_FROM_USER_AND_TRAINER = "createTrainerProfileFromUserAndTrainer";
    private static final String CREATE_NOTIFICATION_DTO_FROM_CREDENTIAL = "createNotificationDTOFromCredential";
    private static final String CLASS_NAME = CredentialGenerator.class.getName();
    private static final String CREATE_TRAINING_REPORT_DTO_FROM_TRAINING = "createTrainingReportDTOFromTraining";
    private static final String CREATE_TRAINING_FROM = "createTrainingFrom";
    private static final String CREATE_NOTIFICATION_FROM_REPORT_DTO = "createNotificationFromReportDTO";
    private static final String CREATE_NOTIFICATION_FROM_TRAINEE_PROFILE = "createNotificationFromTraineeProfile";
    public static final String CREATE_NOTIFICATION_FROM_TRAINER_PROFILE = "createNotificationFromTrainerProfile";
    private static final String REGARDS = "\nWith Regards,\nEpam Gym Management";

    private ValueMapper() {
    }

    public static User userDtoToUserMapper(UserDTO userDTO) throws UserException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), USER_DTO_TO_USER_MAPPER, CLASS_NAME, userDTO.toString());
        if(userDTO.getFirstName()==null || userDTO.getLastName()==null || userDTO.getEMail()==null)
            throw new UserException("Invalid user details");
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEMail(userDTO.getEMail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setActive(true);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), USER_DTO_TO_USER_MAPPER, CLASS_NAME);
        return user;
    }

    public static TraineeProfile createTraineeProfileFromUserAndTrainee(User user, Trainee trainee) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CREATE_TRAINEE_PROFILE_FROM_USER_AND_TRAINEE, CLASS_NAME, trainee.toString());
        List<TrainerBasicDetails> trainerBasicDetailsList = new ArrayList<>();
        trainee.getTrainerList().forEach(trainer -> {
            TrainerBasicDetails trainerBasicDetails = new TrainerBasicDetails(trainer.getUser().getUsername(), trainer.getUser().getFirstName(), trainer.getUser().getLastName(), trainer.getTrainingType().getTrainingTypeName());
            trainerBasicDetailsList.add(trainerBasicDetails);
        });
        TraineeProfile traineeProfile = TraineeProfile.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(trainee.getAddress())
                .eMail(user.getEMail())
                .dateOfBirth(trainee.getDateOfBirth())
                .trainersList(trainerBasicDetailsList)
                .isActive(user.isActive())
                .build();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CREATE_TRAINEE_PROFILE_FROM_USER_AND_TRAINEE, CLASS_NAME);
        return traineeProfile;
    }

    public static TrainerProfile createTrainerProfileFromUserAndTrainer(User user, Trainer trainer) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CREATE_TRAINER_PROFILE_FROM_USER_AND_TRAINER, CLASS_NAME, trainer.toString());
        List<TraineeBasicDetails> traineeList = new ArrayList<>();
        trainer.getTraineeList().forEach(trainee -> {
            TraineeBasicDetails traineeBasicDetails = new TraineeBasicDetails(trainee.getUser().getUsername(), trainee.getUser().getFirstName(), trainee.getUser().getLastName());
            traineeList.add(traineeBasicDetails);
        });
        TrainerProfile trainerProfile = TrainerProfile.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .specialization(trainer.getTrainingType().getTrainingTypeName())
                .isActive(user.isActive())
                .eMail(user.getEMail())
                .traineeList(traineeList)
                .build();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CREATE_TRAINER_PROFILE_FROM_USER_AND_TRAINER, CLASS_NAME);
        return trainerProfile;
    }

    public static NotificationDTO createNotificationDTOFromCredential(Credential credential, String toEmail) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CREATE_NOTIFICATION_DTO_FROM_CREDENTIAL, CLASS_NAME, credential.toString() +"  "+ toEmail);

        NotificationDTO notificationDTO =  NotificationDTO.builder()
                .toEmails(List.of(toEmail))
                .ccEmails(new ArrayList<>())
                .subject("Account created Successfully")
                .body("Please use the following credentials for logging in : \n"+credential+REGARDS)
                .build();

        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CREATE_NOTIFICATION_DTO_FROM_CREDENTIAL, CLASS_NAME);
        return notificationDTO;
    }

    public static NotificationDTO createNotificationFromTrainerProfile(TrainerProfile trainerProfile) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CREATE_NOTIFICATION_FROM_TRAINER_PROFILE, CLASS_NAME, trainerProfile.toString());
        NotificationDTO notificationDTO =   NotificationDTO.builder()
                .toEmails(List.of(trainerProfile.getEMail()))
                .ccEmails(new ArrayList<>())
                .subject("Account updated Successfully")
                .body("Please find the updated details below : \n"+trainerProfile+REGARDS)
                .build();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CREATE_NOTIFICATION_FROM_TRAINER_PROFILE, CLASS_NAME);
        return notificationDTO;
    }

    public static NotificationDTO createNotificationFromTraineeProfile(TraineeProfile traineeProfile) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CREATE_NOTIFICATION_FROM_TRAINEE_PROFILE, CLASS_NAME, traineeProfile.toString());
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .toEmails(List.of(traineeProfile.getEMail()))
                .ccEmails(new ArrayList<>())
                .subject("Account updated Successfully")
                .body("Please find the updated details below : \n"+traineeProfile+REGARDS)
                .build();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CREATE_NOTIFICATION_FROM_TRAINEE_PROFILE, CLASS_NAME);
        return notificationDTO;
    }

    public static NotificationDTO createNotificationFromReportDTO(TrainingReportDTO reportDTO) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CREATE_NOTIFICATION_FROM_REPORT_DTO, CLASS_NAME, reportDTO.toString());
        NotificationDTO notificationDTO =  NotificationDTO.builder()
                .toEmails(List.of(reportDTO.getEMail()))
                .ccEmails(new ArrayList<>())
                .subject("New Training Added to your report")
                .body("Please find the training details below : \n"+reportDTO+REGARDS)
                .build();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CREATE_NOTIFICATION_FROM_REPORT_DTO, CLASS_NAME);
        return notificationDTO;
    }

    public static Training createTrainingFrom(TrainingDetails trainingDetails, Trainer trainer, Trainee trainee, TrainingType trainingType) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CREATE_TRAINING_FROM, CLASS_NAME);
        Training training = new Training();
        training.setTrainingDate(trainingDetails.getDate());
        training.setTrainingName(trainingDetails.getName());
        training.setTrainingDuration(trainingDetails.getDuration());
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CREATE_TRAINING_FROM, CLASS_NAME);
        return training;

    }

    public static TrainingReportDTO createTrainingReportDTOFromTraining(Trainer trainer, TrainingDetails trainingDetails) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), CREATE_TRAINING_REPORT_DTO_FROM_TRAINING, CLASS_NAME, trainer.toString());
        TrainingReportDTO reportDTO = TrainingReportDTO.builder()
                .trainerUsername(trainer.getUser().getUsername())
                .trainerFirstName(trainer.getUser().getFirstName())
                .trainerLastName(trainer.getUser().getLastName())
                .eMail(trainer.getUser().getEMail())
                .trainerStatus(trainer.getUser().isActive())
                .trainingDate(trainingDetails.getDate())
                .trainingDuration(trainingDetails.getDuration())
                .build();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), CREATE_TRAINING_REPORT_DTO_FROM_TRAINING, CLASS_NAME);
        return reportDTO;
    }
}
