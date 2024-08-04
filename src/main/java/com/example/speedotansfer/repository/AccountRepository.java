package com.example.speedotansfer.repository;

import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountNumber(String accountNumber);

    @Query(value = "SELECT * FROM accounts WHERE currency = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<Account> findAccountByCurrencyAndUserid(String currency, long userid);

    @Query(value = "SELECT * FROM accounts WHERE user_id = ?1", nativeQuery = true)
    List<Account> findAllByUserid(long userid);

    @Query(value = "SELECT * FROM accounts WHERE user_id = ?1 AND (card_number = ?2 OR currency = ?3)", nativeQuery = true)
    Optional<Account> findAccountByUserIdSameCurrencyOrCardNumber(Long id,String cardNumber, String currency);

    @Query(value="SELECT * FROM accounts WHERE card_number = ?1", nativeQuery = true)
    Optional<Account> findByCardNumber(String cardNumber);

}
