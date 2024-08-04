package com.example.speedotansfer.exception.response;

import com.example.speedotansfer.security.JacksonLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class ErrorDetails {
    @JsonSerialize(using = JacksonLocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private HttpStatus httpStatus;

}
