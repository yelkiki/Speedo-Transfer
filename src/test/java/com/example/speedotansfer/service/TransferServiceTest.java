package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.AccountNotFoundException;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Account;
import com.example.speedotansfer.model.Transaction;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.AccountRepository;
import com.example.speedotansfer.repository.TransactionRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.impl.RedisService;
import com.example.speedotansfer.service.impl.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransferServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private TransferService transferService;

    private User sender;
    private User receiver;
    private Account senderAccount;
    private Account receiverAccount;
    private SendMoneyWithAccNumberDTO sendMoneyWithAccNumberDTO;

    @BeforeEach
    public void setUp() {
        sender = User.builder()
                .internalId(1L)
                .email("sender@example.com")
                .fullName("Sender Name")
                .phoneNumber("01111111111")
                .username("senderUsername")
                .build();

        receiver = User.builder()
                .internalId(2L)
                .email("receiver@example.com")
                .fullName("Receiver Name")
                .phoneNumber("02222222222")
                .username("receiverUsername")
                .build();

        senderAccount = Account.builder()
                .accountNumber("123456789")
                .currency(Currency.USD)
                .balance(500.0)
                .user(sender)
                .build();

        receiverAccount = Account.builder()
                .accountNumber("987654321")
                .currency(Currency.USD)
                .balance(100.0)
                .user(receiver)
                .build();

        sendMoneyWithAccNumberDTO = new SendMoneyWithAccNumberDTO();
        sendMoneyWithAccNumberDTO.setAccountNumber("987654321");
        sendMoneyWithAccNumberDTO.setAmount(100.0);
        sendMoneyWithAccNumberDTO.setSendCurrency(Currency.USD);
        sendMoneyWithAccNumberDTO.setReceiveCurrency(Currency.USD);
    }

    @Test
    public void testTransferUsingAccNumber_Success() throws InsufficientAmountException, UserNotFoundException, AccountNotFoundException {
        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findAccountByAccountNumber("987654321")).thenReturn(Optional.of(receiverAccount));
        when(accountRepository.findAccountByCurrencyAndUserid("USD", 1L)).thenReturn(Optional.of(senderAccount));
        when(userRepository.findUserByAccount(receiverAccount)).thenReturn(Optional.of(receiver));

        TransferResponseDTO response = transferService.transferUsingAccNumber("Bearer token", sendMoneyWithAccNumberDTO);

        assertNotNull(response);
        assertTrue(response.isStatus());
        assertEquals(sendMoneyWithAccNumberDTO.getAmount(), response.getAmount());

        verify(accountRepository, times(1)).save(senderAccount);
        verify(accountRepository, times(1)).save(receiverAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransferUsingAccNumber_InsufficientFunds() {
        sendMoneyWithAccNumberDTO.setAmount(600.0); // More than the sender's balance

        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findAccountByAccountNumber("987654321")).thenReturn(Optional.of(receiverAccount));
        when(accountRepository.findAccountByCurrencyAndUserid("USD", 1L)).thenReturn(Optional.of(senderAccount));
        when(userRepository.findUserByAccount(receiverAccount)).thenReturn(Optional.of(receiver));

        assertThrows(InsufficientAmountException.class, () -> {
            transferService.transferUsingAccNumber("Bearer token", sendMoneyWithAccNumberDTO);
        });

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


    @Test
    public void testTransferUsingAccNumber_ReceiverAccountNotFound() {
        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findAccountByAccountNumber("987654321")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            transferService.transferUsingAccNumber("Bearer token", sendMoneyWithAccNumberDTO);
        });

        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testTransferUsingAccNumber_SenderAccountNotFound() {
        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findAccountByAccountNumber("987654321")).thenReturn(Optional.of(receiverAccount));
        when(accountRepository.findAccountByCurrencyAndUserid("USD", 1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            transferService.transferUsingAccNumber("Bearer token", sendMoneyWithAccNumberDTO);
        });

        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}

