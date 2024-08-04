package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.transactionDTOs.*;
import com.example.speedotansfer.dto.userDTOs.BalanceDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.exception.custom.*;
import com.example.speedotansfer.service.impl.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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



    @Operation(summary = "Get All Transactions")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AllTransactionsDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = UserNotFoundException.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = InvalidJwtTokenException.class), mediaType = "application/json")})

    @GetMapping
    public AllTransactionsDTO getTransactions(@RequestHeader("Authorization") String token)
            throws UserNotFoundException, InvalidJwtTokenException {
        return transactionService.getHistory(token);
    }

    @PostMapping("/account")
    public TransferResponseDTO transferUsingAccountNumber(@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithAccNumberDTO details)
            throws UserNotFoundException, InsufficientAmountException, InvalidTransferException, AccountNotFoundException, InvalidJwtTokenException {
        return transactionService.transferUsingAccNumber(token, details);

    }

//    @PostMapping("/username")
//    public TransferResponseDTO transferUsingUsername(@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithUsernameDTO details) throws UserNotFoundException, InsufficientAmountException {
//        return transactionService.transferUsingUsername(token,details);
//
//    }

    @GetMapping("/convert/{from}/{to}")
    public GetExchangeRateDTO getExchangeRate(@PathVariable Currency from, @PathVariable Currency to){
        return new GetExchangeRateDTO(transactionService.getExchangeRate(from, to));
    }


}
