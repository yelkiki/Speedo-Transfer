package com.example.speedotansfer.service;


import com.example.speedotansfer.dto.accountDTO.AccountDTO;
import com.example.speedotansfer.dto.userDTOs.BalanceDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.AccountAlreadyExists;
import com.example.speedotansfer.exception.custom.AccountNotFoundException;
import com.example.speedotansfer.exception.custom.AuthenticationErrorException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.impl.AccountService;
import com.example.speedotansfer.service.impl.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private AccountService accountService;

    private User user;
    private Account account;
    private List<Account> accounts;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .internalId(1L)
                .email("user@example.com")
                .fullName("User Name")
                .phoneNumber("01111111111")
                .username("username")
                .build();

        account = Account.builder()
                .accountNumber("1234567890")
                .balance(100.0)
                .currency(Currency.USD)
                .user(user)
                .build();

        accounts = new ArrayList<>();
        accounts.add(account);
    }

    @Test
    public void testGetBalance_Success() {
        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(accountRepository.findAllByUserid(anyLong())).thenReturn(accounts);

        BalanceDTO balanceDTO = accountService.getBalance("Bearer token");

        assertNotNull(balanceDTO);
        assertEquals(100.0, balanceDTO.getBalance());
    }


    @Test
    public void testGetBalanceUsingAccountNumber_Success() throws AccountNotFoundException {
        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.of(account));

        BalanceDTO balanceDTO = accountService.getBalanceUsingAccountNumber("Bearer token", "1234567890");

        assertNotNull(balanceDTO);
        assertEquals(100.0, balanceDTO.getBalance());
    }


    @Test
    public void testGetBalanceUsingAccountNumber_AccountNotFound() {
        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getBalanceUsingAccountNumber("Bearer token", "1234567890");
        });
    }


    @Test
    public void testAddAccount_Success() throws AccountAlreadyExists, UserNotFoundException {
        AccountDTO accountDTO = AccountDTO.builder()
                .cardNumber("1234567890123456")
                .currency(Currency.USD)
                .cardholderName("User Name")
                .cvv(123)
                .expirationDate("12/25")
                .build();

        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(anyLong())).thenReturn(Optional.of(user));
        when(accountRepository.findByCardNumber(anyString())).thenReturn(Optional.empty());
        when(accountRepository.findAccountByUserIdSameCurrencyOrCardNumber(anyLong(), anyString(), anyString())).thenReturn(Optional.empty());

        AccountDTO response = accountService.addAccount("Bearer token", accountDTO);

        assertNotNull(response);
        assertEquals("User Name", response.getCardholderName());
    }


    @Test
    public void testAddAccount_AccountAlreadyExists() {
        AccountDTO accountDTO = AccountDTO.builder()
                .cardNumber("1234567890123456")
                .currency(Currency.USD)
                .cardholderName("User Name")
                .cvv(123)
                .expirationDate("12/25")
                .build();

        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(anyLong())).thenReturn(Optional.of(user));
        when(accountRepository.findByCardNumber(anyString())).thenReturn(Optional.of(account));

        assertThrows(AccountAlreadyExists.class, () -> {
            accountService.addAccount("Bearer token", accountDTO);
        });

        verify(accountRepository, never()).save(any());
    }
}
