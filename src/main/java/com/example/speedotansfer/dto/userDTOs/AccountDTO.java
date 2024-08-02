package com.example.speedotansfer.dto.userDTOs;

import com.example.speedotansfer.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private String accountNumber;

    private double balance;

    private long userId;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NonNull
    private String cardNumber;
    @NonNull
    private String cardholderName;
    @NonNull
    private int cvv;
    @NonNull
    private String expirationDate;
}
