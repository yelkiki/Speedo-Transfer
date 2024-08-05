package com.example.speedotansfer.dto.transactionDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMoneyWithUsernameDTO {
    @NotNull
    private String username;
    @NotNull
    private Double amount;
}
