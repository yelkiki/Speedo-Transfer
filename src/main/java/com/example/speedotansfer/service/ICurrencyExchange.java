package com.example.speedotansfer.service;

import com.example.speedotansfer.enums.Currency;

public interface ICurrencyExchange {
    public double getExchangeRate(Currency from, Currency to);
}
