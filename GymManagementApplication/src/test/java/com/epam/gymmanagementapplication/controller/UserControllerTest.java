package com.epam.gymmanagementapplication.controller;

import com.epam.gymmanagementapplication.dto.request.ModifyPassword;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.service.UserServiceImpl;
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

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;


    @Test
    void testLoginUser() throws Exception {
        Credential credential = new Credential("username123", "password123");
        Mockito.when(userService.loginUser(any(Credential.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/gym/user/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(credential))).andExpect(status().isAccepted());
        Mockito.verify(userService).loginUser(any(Credential.class));
    }

    @Test
    void testChangePassword() throws Exception {
        ModifyPassword modifyPassword = new ModifyPassword("username123", "oldPassword", "newPassword");
        Mockito.when(userService.changeLogin(any(ModifyPassword.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.put("/gym/user/modifyPassword").contentType(MediaType.APPLICATION_JSON).content(asJsonString(modifyPassword))).andExpect(status().isAccepted());
        Mockito.verify(userService).changeLogin(any(ModifyPassword.class));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }


}
