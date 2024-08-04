package com.example.speedotansfer.dto.authDTOs;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RegisterReponseDTO {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private HttpStatusCode httpStatus;
}
