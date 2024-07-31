package com.example.speedotansfer.dto.transactionDTOs;

import lombok.Data;

@Data
public class SendMoneyWithUsernameDTO {
    private String username;
    private double amount;
}
