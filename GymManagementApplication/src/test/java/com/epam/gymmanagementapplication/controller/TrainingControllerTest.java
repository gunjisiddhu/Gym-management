package com.epam.gymmanagementapplication.controller;


import com.epam.gymmanagementapplication.dto.request.NotificationDTO;
import com.epam.gymmanagementapplication.dto.request.TrainingReportDTO;
import com.epam.gymmanagementapplication.dto.response.TrainingDetails;
import com.epam.gymmanagementapplication.kafka.KafkaProducer;
import com.epam.gymmanagementapplication.service.TrainingServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingServiceImpl trainingService;



    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    void testAddNewTraining() throws Exception {
        TrainingDetails trainingDetails = new TrainingDetails("Training1", LocalDate.now(), 1, 60, "Trainer1", "Trainee1");
        TrainingReportDTO reportDTO = new TrainingReportDTO("siddhu", "Siddi", "saai", "sid@gmail.com", true, LocalDate.parse("2023-11-11"), 20L);

        Mockito.when(trainingService.add(any(TrainingDetails.class))).thenReturn(reportDTO);
        Mockito.doNothing().when(kafkaProducer).sendTrainingReport(any(TrainingReportDTO.class));
        Mockito.doNothing().when(kafkaProducer).sendNotification(any(NotificationDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/training/").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainingDetails))).andExpect(status().isOk());

        Mockito.verify(kafkaProducer).sendNotification(any(NotificationDTO.class));
        Mockito.verify(kafkaProducer).sendTrainingReport(any(TrainingReportDTO.class));

    }

    // Utility method to convert objects to JSON
    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}
