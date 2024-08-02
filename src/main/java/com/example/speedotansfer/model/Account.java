package com.example.speedotansfer.model;


import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column()
    private double balance = 0;

    @Column()
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.EGY;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(unique = true, nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cardholderName;

    @Column(nullable = false)
    private int cvv;

    @Column(nullable = false)
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Expiration date must be in MM/YY format")
    private String expirationDate;

    public AccountDTO toDTO(){
        return AccountDTO.builder()
                .accountNumber(accountNumber)
                .balance(balance)
                .currency(currency)
                .userId(user.getInternalId())
                .cardNumber(cardNumber)
                .cardholderName(cardholderName)
                .cvv(cvv)
                .expirationDate(expirationDate)
                .build();
    }
}
