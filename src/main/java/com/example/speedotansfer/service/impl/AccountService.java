package com.example.speedotansfer.service.impl;

import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.exception.custom.AccountAlreadyExists;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.security.JwtUtils;
import com.example.speedotansfer.service.IAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountService implements IAccount {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;



    @Override
    public AccountDTO addAccount(String token, AccountDTO acc)
            throws AccountAlreadyExists ,UserNotFoundException, InvalidJwtTokenException {

        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        Optional<Account> res = accountRepository.
                findAccountByUserIdSameCurrencyOrCardNumber( acc.getCardNumber(),acc.getCurrency());

        if(res.isPresent()){
            throw new AccountAlreadyExists("You Already Have a card with same number or currency");
        }

        Account account = Account.builder()
                .currency(acc.getCurrency())
                .balance(100)
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .cardholderName(acc.getCardholderName())
                .cardNumber(acc.getCardNumber())
                .cvv(acc.getCvv())
                .expirationDate(acc.getExpirationDate())
                .user(user)
                .build();

        accountRepository.save(account);

        return account.toDTO();
    }
}
