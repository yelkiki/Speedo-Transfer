package com.example.speedotansfer.service;

public interface IToken {
    public void revokeToken(String token);

    public void revokeAllTokensByUserId(Long userId);

    public void revokeTokenById(Long id);
}
