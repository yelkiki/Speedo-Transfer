package com.example.speedotansfer.dto.userDTOs;

import lombok.Data;


@Data
public class UpdateUserDTO {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
}
