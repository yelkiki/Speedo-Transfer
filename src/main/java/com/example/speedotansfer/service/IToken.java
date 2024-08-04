package com.example.speedotansfer.service;

import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;

public interface IToken {
    public void revokeToken(String token) throws InvalidJwtTokenException;

    public void revokeAllTokensByUserId(Long userId);

    public void revokeTokenById(Long id) throws InvalidJwtTokenException;
}
