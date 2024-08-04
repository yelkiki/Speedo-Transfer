package com.example.speedotansfer.service.impl.helpers;

import java.security.SecureRandom;

public class AccountNumberGenerator {
    public static String generateNumber() {
        StringBuilder sb = new StringBuilder(15);
        sb.append(new SecureRandom().nextInt(1,10));
        for (int i = 1; i < 15; i++) {
            sb.append(new SecureRandom().nextInt(10));
        }
        return sb.toString();
    }
}
