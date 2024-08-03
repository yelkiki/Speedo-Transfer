package com.example.speedotansfer.exception.custom;

public class AccountAlreadyExists extends RuntimeException{
    public AccountAlreadyExists(String message) {
        super(message);
    }
}
