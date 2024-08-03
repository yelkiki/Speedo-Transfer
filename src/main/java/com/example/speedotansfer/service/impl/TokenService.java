package com.example.speedotansfer.service.impl;

import com.example.speedotansfer.repository.TokenRepository;
import com.example.speedotansfer.service.IToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService implements IToken {

    private final TokenRepository tokenRepository;

    @Override
    public void revokeToken(String token) {
        
    }

    @Override
    public void revokeAllTokensByUserId(Long userId) {

    }

    @Override
    public void revokeTokenById(Long id) {

    }
}
