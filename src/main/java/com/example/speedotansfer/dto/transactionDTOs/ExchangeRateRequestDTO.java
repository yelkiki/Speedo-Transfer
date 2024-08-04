package com.example.speedotansfer.dto.transactionDTOs;

import com.example.speedotansfer.enums.Currency;
import lombok.Data;

@Data
public class ExchangeRateRequestDTO {
    Currency from;
    Currency to;
}
