package com.example.speedotansfer.dto.userDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    private final String username;
    private final String fullName;
    @Email
    private final String email;
    @Pattern(regexp = "^(\\+201|01|00201)[0-2,5][0-9]{8}")
    private final String phoneNumber;
}
