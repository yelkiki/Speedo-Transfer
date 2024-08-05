package com.example.speedotansfer.controller;

import com.example.speedotansfer.dto.authDTOs.RegisterDTO;
import com.example.speedotansfer.exception.custom.UserAlreadyExistsException;
import com.example.speedotansfer.service.impl.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authenticator;

    @Test
    void testRegisterUserInvalidData() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@gmail.com\" , \"password\": \"12345\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserValidData() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"youssef elkiki\",\"email\": \"y@gmail.com\",\"phoneNumber\": \"01151816234\",\"gender\": \"MALE\",\"country\": \"EG\",\"birthDate\": \"1990-01-01\",\"username\": \"y\",\"password\": \"password\",\"confirmPassword\": \"password\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterWithAlreadyExistingUser() throws Exception {
        Mockito.when(this.authenticator.register(any(RegisterDTO.class)))
                .thenThrow(new UserAlreadyExistsException("Customer already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"youssef elkiki\",\"email\": \"y@gmail.com\",\"phoneNumber\": \"01151816234\",\"gender\": \"MALE\",\"country\": \"EG\",\"birthDate\": \"1990-01-01\",\"username\": \"y\",\"password\": \"password\",\"confirmPassword\": \"password\"\n}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterWithNotEnoughDetails() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Abdulrahman\"}"))
                .andExpect(status().isBadRequest());
    }
}

