package com.example.speedotansfer.model;


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
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    // auto Generated --- later 12 numbers
    private String accountNumber;

    @Column()
    private double balance;

    // relation with account
}
