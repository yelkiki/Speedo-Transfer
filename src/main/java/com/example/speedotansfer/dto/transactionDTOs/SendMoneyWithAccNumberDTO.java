package com.example.speedotansfer.dto.transactionDTOs;

import com.example.speedotansfer.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMoneyWithAccNumberDTO {
    @NotNull
    private String accountNumber;
    @NotNull
    private Double amount;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency sendCurrency;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency receiveCurrency;
}
