package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {
    @Override
    Optional<Account> findById(Long aLong);
    Optional<Account> findByAccountNumber(String accountNumber);
}
