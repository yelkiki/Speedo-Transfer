package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
