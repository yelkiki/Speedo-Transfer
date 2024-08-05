package com.example.speedotansfer.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationErrorException extends AuthenticationException {
    public AuthenticationErrorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AuthenticationErrorException(String msg) {
        super(msg);
    }
}
