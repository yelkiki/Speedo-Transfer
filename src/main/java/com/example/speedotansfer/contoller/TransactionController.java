package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.customerDTOs.AccountDTO;
import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithUsernameDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.model.Transaction;
import com.example.speedotansfer.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/balance")
    public AccountDTO getBalance(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        String trimmedToken = token.substring(7).trim();
        return transactionService.getBalance(trimmedToken);
    }



    @GetMapping
    public AllTransactionsDTO getTransactions(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        String trimmedToken = token.substring(7).trim();
        return transactionService.getHistory(trimmedToken);
    }

    @PostMapping("/account")
    public TransferResponseDTO transferUsingAccountNumber(@RequestHeader("Authorization") String token,@RequestBody @Valid SendMoneyWithAccNumberDTO details) throws UserNotFoundException, InsufficientAmountException {
        String trimmedToken = token.substring(7).trim();
        return transactionService.transferUsingAccNumber(trimmedToken,details);

    }

    @PostMapping("/username")
    public TransferResponseDTO transferUsingUsername(@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithUsernameDTO details) throws UserNotFoundException, InsufficientAmountException {
        String trimmedToken = token.substring(7).trim();
        return transactionService.transferUsingUsername(trimmedToken,details);

    }


}
