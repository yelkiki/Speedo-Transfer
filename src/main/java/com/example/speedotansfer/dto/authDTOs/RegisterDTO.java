package com.example.speedotansfer.dto.authDTOs;


import com.example.speedotansfer.enums.Country;
import com.example.speedotansfer.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RegisterDTO {
    @NotNull
    private String fullName;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^(\\+201|01|00201)[0-2,5][0-9]{8}")
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
