package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    String findAccountNumberByEmail(String email);
    Optional<User> findUserByAccountNumber(String accountNumber);
}
