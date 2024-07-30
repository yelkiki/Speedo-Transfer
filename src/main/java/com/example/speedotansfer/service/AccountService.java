package com.example.speedotansfer.service;

import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccount{
    private final AccountRepository accountRepository;

    @Override
    public String deposit(double amount) {
        return "";
    }

    @Override
    public String withdraw(double amount) {
        return "";
    }

    @Override
    public double getBalance(String token) {
        return 0;
    }
}
