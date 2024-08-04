package com.example.speedotansfer.security;

import com.example.speedotansfer.config.JacksonConfig;
import com.example.speedotansfer.exception.response.ErrorDetails;
import com.fasterxml.jackson.core.FormatFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        log.error("Error while handling JWT authentication - {}", authException.getMessage());

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Unauthorized", "Error: Unauthorized", HttpStatus.UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();


        String json = mapper.writeValueAsString(errorDetails);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(json);

    }
}