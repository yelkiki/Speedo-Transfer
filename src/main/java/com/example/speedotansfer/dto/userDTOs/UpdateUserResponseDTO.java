package com.example.speedotansfer.dto.userDTOs;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateUserResponseDTO {
    private LocalDateTime updatedAt;
    private String newToken;
    private String massage;
    private String details;
    private HttpStatusCode httpStatusCode;
}
