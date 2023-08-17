package com.epam.gymauthenticationservice.controller;

import com.epam.gymauthenticationservice.model.AuthRequest;
import com.epam.gymauthenticationservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService service;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void testGetTokenValidUser() throws Exception {
        AuthRequest authRequest = new AuthRequest("username", "password");
        String requestBody = objectMapper.writeValueAsString(authRequest);

        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenAnswer((invocation) -> {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new TestingAuthenticationToken(authRequest.getUsername(), authRequest.getPassword(), authorities);
        });
        Mockito.when(service.generateToken(any(String.class))).thenReturn("Siddhu");


        mockMvc.perform(post("/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testGetTokenInvalidUser() throws Exception {
        AuthRequest authRequest = new AuthRequest("username", "password");
        String requestBody = objectMapper.writeValueAsString(authRequest);

        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenAnswer((invocation) -> new TestingAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        Mockito.when(service.generateToken(any(String.class))).thenReturn("Siddhu");


        mockMvc.perform(post("/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testValidateTokenWithValidToken() throws Exception {
        String token = "valid_token";

        Mockito.doNothing().when(service).validateToken(any(String.class));


        mockMvc.perform(get("/auth/validate")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid"));
    }

    @Test
    void testValidateTokenWithInvalidToken() throws Exception {
        String token = "invalid_token";
        Mockito.doThrow(ExpiredJwtException.class).when(service).validateToken(any(String.class));

        mockMvc.perform(get("/auth/validate")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("token invalid"));
    }

    // Add more integration test cases as necessary
}
