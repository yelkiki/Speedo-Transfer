package com.example.speedotansfer.dto.authDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String email;
    private String tokenType;
    private String massage;
    private HttpStatusCode status;
}
