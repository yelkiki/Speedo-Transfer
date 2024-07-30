package com.example.speedotansfer.model;


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
@Table(name = "account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    // auto Generated --- later 12 numbers
    private String accountNumber;

    @Column()
    private double balance = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.EGY;

    @OneToOne(mappedBy = "account")
    private User user;

    // relation with account
}
