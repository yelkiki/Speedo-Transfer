package com.example.speedotansfer.service;

import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Transaction;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.TransactionRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.impl.RedisService;
import com.example.speedotansfer.service.impl.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private TransactionService transactionService;

    private User user;
    private List<Transaction> sentTransactions;
    private List<Transaction> receivedTransactions;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .internalId(1L)
                .email("user@example.com")
                .fullName("User Name")
                .phoneNumber("01111111111")
                .username("username")
                .build();

        sentTransactions = new ArrayList<>();
        receivedTransactions = new ArrayList<>();

        Transaction sentTransaction = Transaction.builder()
                .id(1L)
                .sender(user)
                .receiver(new User())
                .amount(100.0)
                .status(true)
                .currency(Currency.USD)
                .build();

        Transaction receivedTransaction = Transaction.builder()
                .id(2L)
                .sender(new User())
                .receiver(user)
                .amount(200.0)
                .status(true)
                .currency(Currency.USD)
                .build();

        sentTransactions.add(sentTransaction);
        receivedTransactions.add(receivedTransaction);
    }

    @Test
    public void testGetHistory_Success() throws UserNotFoundException {
        when(redisService.exists(anyString())).thenReturn(true);
        when(redisService.getUserIdByToken(anyString())).thenReturn(1L);
        when(userRepository.findUserByInternalId(anyLong())).thenReturn(Optional.of(user));
        when(transactionRepository.findAllBySenderInternalId(anyLong())).thenReturn(sentTransactions);
        when(transactionRepository.findAllByReceiverInternalId(anyLong())).thenReturn(receivedTransactions);

        AllTransactionsDTO response = transactionService.getHistory("Bearer token");

        assertNotNull(response);
        assertEquals(2, response.getTransactions().size());
    }

}

