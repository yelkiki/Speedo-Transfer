package com.example.speedotansfer.repository;

import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "SELECT t from tokens as t JOIN users as u ON u.user_id = t.user_id WHERE u.user_id = ?1 AND t.revoked = false",
            nativeQuery = true)
    List<Token> findAllValidTokensByUserId(Long userId);

    Optional<Token> findByToken(String token) throws InvalidJwtTokenException;
}
