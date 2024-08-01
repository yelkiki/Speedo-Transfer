package com.example.speedotansfer.model;

import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
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

    @CreationTimestamp
    private final LocalDateTime timeStamp = LocalDateTime.now();



    @ManyToOne
    @JoinColumn(name="sender_id", nullable=false)
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id", nullable=false)
    private User receiver;


    public TransferResponseDTO toDto(){
        return TransferResponseDTO.builder()
                .transactionId(this.id)
                .fromAccount(this.sender.getUsername())
                .toAccount(this.receiver.getUsername())
                .amount(this.amount)
                .status(this.status)
                .timestamp(this.timeStamp)
                .build();
    }


}
