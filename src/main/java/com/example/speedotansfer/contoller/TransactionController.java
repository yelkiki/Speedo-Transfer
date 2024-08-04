package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.dto.transactionDTOs.GetExchangeRateDTO;
import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.AccountNotFoundException;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.InvalidJwtTokenException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.service.impl.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
@Validated
public class TransactionController {
    private final TransactionService transactionService;


    @Operation(summary = "Get All Transactions")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AllTransactionsDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = UserNotFoundException.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = InvalidJwtTokenException.class), mediaType = "application/json")})

    @GetMapping
    public AllTransactionsDTO getTransactions(@RequestHeader("Authorization") String token)
            throws UserNotFoundException, AuthenticationException {
        return transactionService.getHistory(token);
    }

    @PostMapping("/account")
    public TransferResponseDTO transferUsingAccountNumber(@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithAccNumberDTO details)
            throws UserNotFoundException, InsufficientAmountException, AccountNotFoundException, AuthenticationException {
        return transactionService.transferUsingAccNumber(token, details);

    }

//    @PostMapping("/username")
//    public TransferResponseDTO transferUsingUsername(@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithUsernameDTO details) throws UserNotFoundException, InsufficientAmountException {
//        return transactionService.transferUsingUsername(token,details);
//
//    }

    @GetMapping("/convert/{from}/{to}")
    public GetExchangeRateDTO getExchangeRate(@PathVariable Currency from, @PathVariable Currency to) {
        return new GetExchangeRateDTO(transactionService.getExchangeRate(from, to));
    }


}
