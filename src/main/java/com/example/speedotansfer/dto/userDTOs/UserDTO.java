package com.example.speedotansfer.dto.userDTOs;

import com.example.speedotansfer.enums.Gender;
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

    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private LocalDate birthDate;
    private String username;

}

