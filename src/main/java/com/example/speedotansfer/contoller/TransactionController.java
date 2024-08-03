package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.dto.userDTOs.BalanceDTO;
import com.example.speedotansfer.exception.custom.AccountNotFoundException;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.InvalidTransferException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.service.impl.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/balance")
    public BalanceDTO getBalance(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        return transactionService.getBalance(token);
    }


    @GetMapping
    public AllTransactionsDTO getTransactions(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        return transactionService.getHistory(token);
    }

    @PostMapping("/account")
    public TransferResponseDTO transferUsingAccountNumber(@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithAccNumberDTO details) throws UserNotFoundException, InsufficientAmountException, InvalidTransferException, AccountNotFoundException {
        return transactionService.transferUsingAccNumber(token, details);

    }

//    @PostMapping("/username")
//    public TransferResponseDTO transferUsingUsername(@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithUsernameDTO details) throws UserNotFoundException, InsufficientAmountException {
//        return transactionService.transferUsingUsername(token,details);
//
//    }


}
