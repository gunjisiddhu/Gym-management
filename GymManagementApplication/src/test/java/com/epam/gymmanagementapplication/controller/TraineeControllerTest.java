package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.*;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TraineeProfile;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.kafka.KafkaProducer;
import com.epam.gymmanagementapplication.service.TraineeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TraineeController.class)
class TraineeControllerTest  {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TraineeService traineeService;
    @MockBean
    KafkaProducer kafkaProducer;


    @Test
    void testAddNewTrainee() throws Exception {
        TraineeDetails traineeDetails = new TraineeDetails("John", "Doe", "john@example.com", LocalDate.parse("2023-05-25"), "Address");
        Credential credential = new Credential("john123", "password");
        Mockito.when(traineeService.addNewTrainee(any(TraineeDetails.class))).thenReturn(credential);
        mockMvc.perform(MockMvcRequestBuilders.post("/gym/trainee/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(traineeDetails))).andExpect(status().isCreated());
        Mockito.verify(kafkaProducer).sendNotification(any(NotificationDTO.class));
    }


    @Test
    void testGetTraineeProfile() throws Exception {
        String username = "john123";
        TraineeProfile traineeProfile = new TraineeProfile("John", "Doe", LocalDate.now(), "Address", "john@example.com", true, null);
        Mockito.when(traineeService.getTraineeProfile(username)).thenReturn(traineeProfile);
        mockMvc.perform(MockMvcRequestBuilders.get("/gym/trainee/").param("username", username)).andExpect(status().isOk());
        Mockito.verify(traineeService).getTraineeProfile(username);
    }


    @Test
    void testUpdateTraineeDetails() throws Exception {
        TraineeProfileUpdate traineeProfileUpdate = new TraineeProfileUpdate("john123", "John", "Doe", "john@example.com", LocalDate.now(), "Address", true);
        TraineeProfile updatedTraineeProfile = new TraineeProfile("John", "Doe", LocalDate.now(), "Updated Address", "john@example.com", true, null);
        Mockito.when(traineeService.updateTrainee(any(TraineeProfileUpdate.class))).thenReturn(updatedTraineeProfile);
        mockMvc.perform(MockMvcRequestBuilders.put("/gym/trainee/").contentType(MediaType.APPLICATION_JSON).content(asJsonString(traineeProfileUpdate))).andExpect(status().isOk());
        Mockito.verify(kafkaProducer).sendNotification(any(NotificationDTO.class));
    }

    @Test
    void testRemoveTrainee() throws Exception {
        String username = "john123";
        mockMvc.perform(MockMvcRequestBuilders.delete("/gym/trainee/").param("username", username)).andExpect(status().isNoContent());
        Mockito.verify(traineeService).removeTrainee(username);
    }


    @Test
    void testUpdateTrainers() throws Exception {
        TraineeTrainersList trainersList = new TraineeTrainersList("john123", List.of("trainer1", "trainer2"));
        List<TrainerProfile> updatedTrainerProfiles = List.of(new TrainerProfile("siddhu", "sasi", "ZUmba", true, "sid@gmail.com", Collections.emptyList()), new TrainerProfile("siddhu", "sasi", "ZUmba", true, "sid@gmail.com", Collections.emptyList()));
        Mockito.when(traineeService.updateTrainersForTrainee(any(TraineeTrainersList.class))).thenReturn(updatedTrainerProfiles);
        mockMvc.perform(MockMvcRequestBuilders.put("/gym/trainee/updateTrainers").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainersList))).andExpect(status().isOk());

    }

    @Test
    void testTrainingList() throws Exception {
        TraineeTrainings traineeTrainings = new TraineeTrainings("john123", LocalDate.parse("2020-11-11"), LocalDate.parse("2020-11-11").plusDays(7), "trainer1", 1);
        List<TrainingDetails> trainingDetailsList = List.of(new TrainingDetails("Training1", LocalDate.now(), 1, 10, "siddhu", "sasi"));
        Mockito.when(traineeService.getTrainings(any(TraineeTrainings.class))).thenReturn(trainingDetailsList);
        mockMvc.perform(MockMvcRequestBuilders.post("/gym/trainee/trainings").contentType(MediaType.APPLICATION_JSON).content(asJsonString(traineeTrainings))).andExpect(status().isOk());
    }

    @Test
    void testGetFreeTrainers() throws Exception {
        String username = "john123";
        List<TrainerProfile> freeTrainers = List.of(new TrainerProfile("trainer1", "Trainer1", "LastName1", true, "test@gmail.com", Collections.emptyList()), new TrainerProfile("trainer2", "Trainer2", "LastName2", true, "Test@gmail.com", Collections.emptyList()));
        Mockito.when(traineeService.getOtherTrainersOtherThanCurrentTrainee(username)).thenReturn(freeTrainers);
        mockMvc.perform(MockMvcRequestBuilders.get("/gym/trainee/freeTrainers").param("username", username)).andExpect(status().isOk());
    }


    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }


}
