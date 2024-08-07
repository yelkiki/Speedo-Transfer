package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySenderInternalId(Long id);
    List<Transaction> findAllByReceiverInternalId(Long id);
}
