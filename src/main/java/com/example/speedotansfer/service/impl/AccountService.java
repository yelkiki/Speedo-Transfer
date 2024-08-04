package com.example.speedotansfer.service.impl;

import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.BalanceDTO;
import com.example.speedotansfer.exception.custom.AccountAlreadyExists;
import com.example.speedotansfer.exception.custom.AccountNotFoundException;
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

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountService implements IAccount {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;


    @Override
    public BalanceDTO getBalance(String token) throws UserNotFoundException, InvalidJwtTokenException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Account> accounts = accountRepository.findAllByUserid(id);
        double balance = accounts.stream()
                .mapToDouble(account -> {
                    switch (account.getCurrency()) {
                        case USD:
                            return account.getBalance() * 48;
                        case EUR:
                            return account.getBalance() * 53;
                        default:
                            return account.getBalance();
                    }
                })
                .sum();


        return new BalanceDTO(balance);
    }

    @Override
    public BalanceDTO getBalanceUsingAccountNumber(String token, String accountNumber) throws UserNotFoundException, InvalidJwtTokenException, AccountNotFoundException, AuthenticationException {
        token = token.substring(7);
        long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFoundException("Can't account with this number"));

        if (account.getUser().getInternalId() != id){
            throw new AuthenticationException("You are not authorized to view this Account");
        }
        return new BalanceDTO(account.getBalance());
    }


    @Override
    public AccountDTO addAccount(String token, AccountDTO acc)
            throws AccountAlreadyExists ,UserNotFoundException, InvalidJwtTokenException {

        token = token.substring(7);
        Long id = jwtUtils.getIdFromJwtToken(token);
        User user = userRepository.
                findUserByInternalId(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        // Assume Account can have only one card
        if(accountRepository.findByCardNumber(acc.getCardNumber()).isPresent()){
            throw new AccountAlreadyExists("Error Adding Card Number");
        }

        Optional<Account> res = accountRepository.
                findAccountByUserIdSameCurrencyOrCardNumber(id,acc.getCardNumber(),acc.getCurrency().toString());

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
