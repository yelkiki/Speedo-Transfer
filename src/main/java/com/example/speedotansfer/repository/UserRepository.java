package com.example.speedotansfer.repository;

import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByAccount(Account account);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    @Query(value="SELECT users.* FROM users JOIN accounts on users.id = accounts.user_id AND accounts.account_number = ?1", nativeQuery = true)
    Optional<User> getUserFromAccountNumber(String accountNumber);
}
