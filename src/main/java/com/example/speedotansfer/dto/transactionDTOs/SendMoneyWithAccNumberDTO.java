package com.example.speedotansfer.dto.transactionDTOs;

import com.example.speedotansfer.enums.Currency;
import lombok.Data;

@Data
public class SendMoneyWithAccNumberDTO {
    private String accountNumber;
    private double amount;
    private Currency sendCurrency;
    private Currency receiveCurrency;
}
