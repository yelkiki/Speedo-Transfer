package com.example.speedotansfer.service.impl.helpers;

import com.example.speedotansfer.enums.Currency;

import java.util.HashMap;
import java.util.Map;

public class CurrencyExchangeService {
    private static final Map<String, Double> exchangeRates = new HashMap<>();

    static {
        exchangeRates.put("EGY-USD", 0.02);
        exchangeRates.put("USD-EGY", 48.65);
        exchangeRates.put("EGY-EUR", 0.02);
        exchangeRates.put("EUR-EGY", 52.96);
        exchangeRates.put("USD-EUR", 0.92);
        exchangeRates.put("EUR-USD", 1.09);
    }

    public static double getExchangeRate(Currency from, Currency to) {
        if (from == to) {
            return 1.0;
        }
        return exchangeRates.getOrDefault(from.name() + "-" + to.name(), 1.0);
    }
}

