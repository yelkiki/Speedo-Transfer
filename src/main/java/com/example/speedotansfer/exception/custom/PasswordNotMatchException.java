package com.example.speedotansfer.exception.custom;

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
