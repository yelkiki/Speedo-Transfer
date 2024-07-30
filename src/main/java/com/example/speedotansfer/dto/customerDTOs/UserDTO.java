package com.example.speedotansfer.dto.customerDTOs;

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
    private String phone;
    private Gender gender;
    private LocalDate birthDate;
    private String username;
    private String accNumber;


}

