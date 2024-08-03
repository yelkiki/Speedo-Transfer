package com.example.speedotansfer.security;

import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.model.Token;
import com.example.speedotansfer.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor

public class JwtUtils {

    private final TokenRepository tokenRepository;
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration.ms}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        // Get Current Authenticated User
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setId(userPrincipal.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Long getIdFromJwtToken(String token) {
        return Long.parseLong(Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getId());
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Token token = tokenRepository.findByToken(authToken).
                    orElseThrow(() -> new InvalidJwtTokenException("Invalid Token"));
            if (token.isRevoked())
                throw new InvalidJwtTokenException("Token is revoked");
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException | InvalidJwtTokenException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("JWT signature does not match locally computed signature: {}", e.getMessage());
        }
        return false;
    }
}
