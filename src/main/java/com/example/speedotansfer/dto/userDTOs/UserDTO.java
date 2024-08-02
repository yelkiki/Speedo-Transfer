package com.example.speedotansfer.dto.userDTOs;

import com.example.speedotansfer.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID userId;
    private String fullName;
    @Email
    private String email;
    @Pattern(regexp = "^(\\+201|01|00201)[0-2,5][0-9]{8}")
    private String phoneNumber;
    private Gender gender;
    private LocalDate birthDate;
    private String username;

}

