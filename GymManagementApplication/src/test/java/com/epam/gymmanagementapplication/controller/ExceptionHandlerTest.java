package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.TraineeTrainings;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.dto.response.TrainerProfile;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.exception.*;
import com.epam.gymmanagementapplication.service.TraineeServiceImpl;
import com.epam.gymmanagementapplication.service.TrainerServiceImpl;
import com.epam.gymmanagementapplication.service.TrainingServiceImpl;
import com.epam.gymmanagementapplication.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class ExceptionHandlerTest  {

    @MockBean
    TraineeServiceImpl traineeService;
    @MockBean
    TrainerServiceImpl trainerService;
    @MockBean
    TrainingServiceImpl trainingService;
    @MockBean
    UserServiceImpl userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testTrainingListWithInvalidTrainee() throws Exception {
        TraineeTrainings traineeTrainings = new TraineeTrainings("Siddhu", LocalDate.parse("2001-11-11"), LocalDate.parse("2001-11-13"), "siddhu", 1);


        Mockito.when(traineeService.getTrainings(any(TraineeTrainings.class))).thenThrow(new TraineeException("Invalid Trainee"));

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/trainee/trainings").contentType(MediaType.APPLICATION_JSON).content(asJsonString(traineeTrainings))).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testGetTrainerProfile() throws Exception {
        String username = "validUsername";
        TrainerProfile trainerProfile = new TrainerProfile();
        trainerProfile.setFirstName("John");
        trainerProfile.setLastName("Doe");


        Mockito.when(trainerService.getTrainerProfile((username))).thenThrow(TrainerException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/gym/trainer/").param("username", username)).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testTrainingException() throws Exception {
        TrainingDetails trainingDetails = new TrainingDetails("Sid", LocalDate.parse("2011-11-11"), 1, 20, "siddhu", "Ammmu");


        Mockito.when(trainingService.add(any(TrainingDetails.class))).thenThrow(TrainingException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/training/").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainingDetails))).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testUserException() throws Exception {
        Credential credential = new Credential("Siddhu", "Qwerty@1991");
        Mockito.when(userService.loginUser(any(Credential.class))).thenThrow(UserException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/user/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(credential))).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testTrainingTypeException() throws Exception {
        TrainingDetails trainingDetails = new TrainingDetails("Sid", LocalDate.parse("2011-11-11"), 1, 20, "siddhu", "Ammmu");


        Mockito.when(trainingService.add(any(TrainingDetails.class))).thenThrow(TrainingTypeException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/training/").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainingDetails))).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testRuntimeException() throws Exception {
        TrainingDetails trainingDetails = new TrainingDetails("Sid", LocalDate.parse("2011-11-11"), 1, 20, "siddhu", "Ammmu");


        Mockito.when(trainingService.add(any(TrainingDetails.class))).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/training/").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainingDetails))).andExpect(MockMvcResultMatchers.status().isInternalServerError());

    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }


}
