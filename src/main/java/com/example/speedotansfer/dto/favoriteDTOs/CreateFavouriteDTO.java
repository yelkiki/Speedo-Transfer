package com.example.speedotansfer.dto.favoriteDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFavouriteDTO {
    @NotNull
    private String fullName;
    @NotNull
    private String accountNumber;
}
