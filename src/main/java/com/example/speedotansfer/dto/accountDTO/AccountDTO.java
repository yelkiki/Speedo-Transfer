package com.example.speedotansfer.dto.accountDTO;

import com.example.speedotansfer.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

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
