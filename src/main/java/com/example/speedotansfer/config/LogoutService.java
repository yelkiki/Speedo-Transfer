package com.example.speedotansfer.config;

import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.model.Token;
import com.example.speedotansfer.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        final String jwt = authHeader.substring(7);
        try {
            Token token = tokenRepository.findByToken(jwt)
                    .orElseThrow(() -> new InvalidJwtTokenException("Invalid Token"));
            // We can delete the token from the database ?? or it will cause problems ?
            token.setRevoked(true);
            tokenRepository.save(token);
        } catch (InvalidJwtTokenException e) {
            throw new RuntimeException(e);
        }
    }
}
