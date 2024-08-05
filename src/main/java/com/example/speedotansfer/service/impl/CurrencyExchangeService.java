package com.example.speedotansfer.service.impl;

import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.service.ICurrencyExchange;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeService implements ICurrencyExchange {
    @Override
    public double getExchangeRate(Currency from, Currency to) {
        return com.example.speedotansfer.service.impl.helpers.CurrencyExchangeService.getExchangeRate(from, to);
    }
}
