package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.TrainingTypeDTO;
import com.epam.gymmanagementapplication.service.TrainingTypeServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingTypeController.class)
class TrainingTypeControllerTest {


    @Autowired
    MockMvc mockMvc;
    @MockBean
    TrainingTypeServiceImpl trainingTypeService;


    @Test
    void testAddNewTrainingType() throws Exception {
        // Test data
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO("Stretching");

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/trainingType/").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainingTypeDTO))).andExpect(status().isCreated());

        Mockito.verify(trainingTypeService).add(any(TrainingTypeDTO.class));
    }


    @Test
    void testAddNewTrainingTypeFailCase() throws Exception {
        // Test data
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO("Strength Training");

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/trainingType/").contentType(MediaType.APPLICATION_JSON).content(asJsonString(trainingTypeDTO))).andExpect(status().isBadRequest());
    }

    // Utility method to convert objects to JSON
    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
