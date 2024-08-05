package com.example.speedotansfer.service.impl;

import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.AuthenticationErrorException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Transaction;
import com.example.speedotansfer.model.User;
import com.example.speedotansfer.repository.TransactionRepository;
import com.example.speedotansfer.repository.UserRepository;
import com.example.speedotansfer.service.ITransaction;
import com.example.speedotansfer.service.impl.helpers.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransaction {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;


    @Override
    public AllTransactionsDTO getHistory(String token) throws UserNotFoundException {
        token = token.substring(7);

        if (!redisService.exists(token))
            throw new AuthenticationErrorException("Unauthorized");

        long id = redisService.getUserIdByToken(token);

        User user = userRepository.findUserByInternalId(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Transaction> lst = transactionRepository.findAllBySenderInternalId(user.getInternalId());
        lst.addAll(transactionRepository.findAllByReceiverInternalId(user.getInternalId()));


        return new AllTransactionsDTO(lst.stream().map(Transaction::toDto).collect(Collectors.toList()));
    }


    @Override
    public double getExchangeRate(Currency from, Currency to) {
        return CurrencyExchangeService.getExchangeRate(from, to);
    }
}
