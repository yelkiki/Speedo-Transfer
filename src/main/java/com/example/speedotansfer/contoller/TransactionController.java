package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithUsernameDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.service.TransactionService;
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
    public AccountDTO getBalance(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        return transactionService.getBalance(token);
    }



    @GetMapping
    public AllTransactionsDTO getTransactions(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        return transactionService.getHistory(token);
    }

    @PostMapping("/account")
    public TransferResponseDTO transferUsingAccountNumber(@RequestHeader("Authorization") String token,@RequestBody @Valid SendMoneyWithAccNumberDTO details) throws UserNotFoundException, InsufficientAmountException {
        return transactionService.transferUsingAccNumber(token,details);

    }

//    @PostMapping("/username")
//    public TransferResponseDTO transferUsingUsername(@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithUsernameDTO details) throws UserNotFoundException, InsufficientAmountException {
//        return transactionService.transferUsingUsername(token,details);
//
//    }


}
