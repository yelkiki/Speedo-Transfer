package com.example.speedotansfer.dto.customerDTOs;

import com.example.speedotansfer.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private long id;

    private String accountNumber;

    private double balance;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    private boolean active;

}
