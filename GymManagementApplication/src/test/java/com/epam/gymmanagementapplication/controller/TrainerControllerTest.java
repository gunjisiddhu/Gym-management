package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.NotificationDTO;
import com.epam.gymmanagementapplication.dto.request.TrainerDetails;
import com.epam.gymmanagementapplication.dto.request.TrainerProfileUpdate;
import com.epam.gymmanagementapplication.dto.request.TrainerTrainings;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.kafka.KafkaProducer;
import com.epam.gymmanagementapplication.service.TrainerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TrainerServiceImpl trainerService;
    @MockBean
    KafkaProducer kafkaProducer;


    @Test
    void testAddNewTrainer() throws Exception {
        TrainerDetails trainerDetails = new TrainerDetails("John", "Doe", "john@example.com", 1);
        Credential credential = new Credential("trainer123", "password");
        Mockito.when(trainerService.addTrainer(any(TrainerDetails.class))).thenReturn(credential);
        mockMvc.perform(MockMvcRequestBuilders.post("/gym/trainer/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainerDetails))).andExpect(status().isCreated());
        Mockito.verify(kafkaProducer).sendNotification(any(NotificationDTO.class));
    }


    @Test
    void testGetTrainerProfile() throws Exception {
        String username = "trainer123";
        TrainerProfile trainerProfile = new TrainerProfile("John", "Doe", "Zumba", true, "john@example.com", Collections.emptyList());
        Mockito.when(trainerService.getTrainerProfile(username)).thenReturn(trainerProfile);
        mockMvc.perform(MockMvcRequestBuilders.get("/gym/trainer/").param("username", username)).andExpect(status().isOk());
        Mockito.verify(trainerService).getTrainerProfile(username);
    }

    @Test
    void testUpdateTrainerDetails() throws Exception {
        TrainerProfileUpdate trainerProfileUpdate = new TrainerProfileUpdate("trainer123", "John", "Doe", "john@example.com", 1, true);
        TrainerProfile updatedTrainerProfile = new TrainerProfile("John", "Doe", "zumba", true, "john@example.com", Collections.emptyList());
        Mockito.when(trainerService.updateTrainer(any(TrainerProfileUpdate.class))).thenReturn(updatedTrainerProfile);
        mockMvc.perform(MockMvcRequestBuilders.put("/gym/trainer/").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainerProfileUpdate))).andExpect(status().isOk());
        Mockito.verify(kafkaProducer).sendNotification(any(NotificationDTO.class));
    }


    @Test
    void testGetTrainings() throws Exception {
        TrainerTrainings trainerTrainings = new TrainerTrainings("trainer123", LocalDate.parse("2022-11-11"), LocalDate.parse("2022-11-11").plusDays(7), "trainee456");
        List<TrainingDetails> trainingDetailsList = List.of(new TrainingDetails("Training1", LocalDate.now(), 1, 60, "Trainer1", "Trainee1"), new TrainingDetails("Training2", LocalDate.now().plusDays(1), 2, 45, "Trainer2", "Trainee2"));
        Mockito.when(trainerService.getTrainings(any(TrainerTrainings.class))).thenReturn(trainingDetailsList);
        mockMvc.perform(MockMvcRequestBuilders.post("/gym/trainer/trainingList").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainerTrainings))).andExpect(status().isOk());
    }


    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }


}
