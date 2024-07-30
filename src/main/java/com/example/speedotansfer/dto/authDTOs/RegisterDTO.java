package com.example.speedotansfer.dto.authDTOs;


import com.example.speedotansfer.enums.Country;
import com.example.speedotansfer.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RegisterDTO {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Country country;
    @NotNull
    private LocalDate birthDate;

}
