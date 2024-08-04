package com.example.speedotansfer.service.impl;

import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.model.Token;
import com.example.speedotansfer.repository.TokenRepository;
import com.example.speedotansfer.service.IToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService implements IToken {

    private final TokenRepository tokenRepository;

    @Override
    public void revokeToken(String token)
            throws InvalidJwtTokenException {
        Token tokenEntity = tokenRepository.findByToken(token)
                .orElseThrow(()-> new InvalidJwtTokenException("Invalid token"));
        tokenEntity.setRevoked(true);
        tokenRepository.save(tokenEntity);
    }

    @Override
    public void revokeAllTokensByUserId(Long userId) {
        List<Token>tokens = tokenRepository.findAllValidTokensByUserId(userId);
        for(Token token: tokens){
            token.setRevoked(true);
        }
        tokenRepository.saveAll(tokens);
    }

    @Override
    public void revokeTokenById(Long id) throws InvalidJwtTokenException {
        Token token = tokenRepository.findById(id)
                .orElseThrow(()-> new InvalidJwtTokenException("Invalid token"));
        token.setRevoked(true);
        tokenRepository.save(token);
    }
}
