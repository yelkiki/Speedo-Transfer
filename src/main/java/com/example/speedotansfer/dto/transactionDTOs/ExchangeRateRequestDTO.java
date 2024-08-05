package com.example.speedotansfer.dto.transactionDTOs;

import com.example.speedotansfer.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExchangeRateRequestDTO {
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency from;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency to;
}
