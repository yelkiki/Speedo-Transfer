package com.example.speedotansfer.service.impl;


import com.example.speedotansfer.dto.accountDTO.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.BalanceDTO;
import com.example.speedotansfer.exception.custom.AccountAlreadyExists;
import com.example.speedotansfer.exception.custom.AccountNotFoundException;
import com.example.speedotansfer.exception.custom.AuthenticationErrorException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.IAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.speedotansfer.service.impl.helpers.AccountNumberGenerator.generateNumber;


@Service
@RequiredArgsConstructor
public class AccountService implements IAccount {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RedisService redisService;


    @Override
    public BalanceDTO getBalance(String token) {
        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationErrorException("Unauthorized");


        long id = redisService.getUserIdByToken(token);


        List<Account> accounts = accountRepository.findAllByUserid(id);
        double balance = accounts.stream()
                .mapToDouble(account -> {
                    switch (account.getCurrency()) {
                        case EGY:
                            return account.getBalance() * 0.02;
                        case EUR:
                            return account.getBalance() * 1.09;
                        default:
                            return account.getBalance();
                    }
                })
                .sum();

        return new BalanceDTO(balance);
    }

    @Override
    public BalanceDTO getBalanceUsingAccountNumber(String token, String accountNumber) throws AccountNotFoundException, AuthenticationException {
        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationErrorException("Unauthorized");


        long id = redisService.getUserIdByToken(token);

        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Can't account with this number"));

        if (account.getUser().getInternalId() != id) {
            throw new AuthenticationException("You are not authorized to view this Account") {
            };
        }
        return new BalanceDTO(account.getBalance());
    }


    @Override
    public AccountDTO addAccount(String token, AccountDTO acc)
            throws AccountAlreadyExists, UserNotFoundException {

        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationErrorException("Unauthorized");


        Long id = redisService.getUserIdByToken(token);

        User user = userRepository.
                findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        // Assume Account can have only one card
        if (accountRepository.findByCardNumber(acc.getCardNumber()).isPresent()) {
            throw new AccountAlreadyExists("Error Adding Card Number");
        }

        Optional<Account> res = accountRepository.
                findAccountByUserIdSameCurrencyOrCardNumber(id, acc.getCardNumber(), acc.getCurrency().toString());

        if (res.isPresent()) {
            throw new AccountAlreadyExists("You Already Have a card with same number or currency");
        }

        Account account = Account.builder()
                .currency(acc.getCurrency())
                .balance(100)
                .accountNumber(generateNumber())
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
