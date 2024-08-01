package com.example.speedotansfer.model;


import com.example.speedotansfer.dto.customerDTOs.AccountDTO;
import com.example.speedotansfer.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne(mappedBy = "account")
    private User user;

    public AccountDTO toDTO(){
        return AccountDTO.builder()
                .accountNumber(accountNumber)
                .balance(balance)
                .currency(currency)
                .build();
    }
}
