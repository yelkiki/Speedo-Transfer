package com.example.speedotansfer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "transaction")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private double amount;


    @Column(nullable = false)
    private boolean status;



    /// btoo3 el relationships
    // senderID

    // RecieverID

}
