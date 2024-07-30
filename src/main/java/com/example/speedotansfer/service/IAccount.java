package com.example.speedotansfer.service;

public interface IAccount {
    public String deposit(double amount);
    public String withdraw(double amount);
    public double getBalance(String token);
}
