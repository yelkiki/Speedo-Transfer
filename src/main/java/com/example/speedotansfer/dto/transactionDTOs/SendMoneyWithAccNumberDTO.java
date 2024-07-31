package com.example.speedotansfer.dto.transactionDTOs;

import lombok.Data;

@Data
public class SendMoneyWithAccNumberDTO {
    private String accountNumber;
    private double amount;
}
