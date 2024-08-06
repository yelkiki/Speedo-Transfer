package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.accountDTO.AccountDTO;
import com.example.speedotansfer.dto.accountDTO.AccountNumberDTO;
import com.example.speedotansfer.dto.userDTOs.BalanceDTO;
import com.example.speedotansfer.exception.custom.AccountNotFoundException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.exception.response.ErrorDetails;
import com.example.speedotansfer.service.impl.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Validated
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Add new Account to User")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse
    @PostMapping()
    public AccountDTO addCard(@RequestHeader("Authorization") String token, @RequestBody AccountDTO acc)
            throws UserNotFoundException {
        return accountService.addAccount(token, acc);
    }

    @Operation(summary = "Get Summation Balance of all accounts in EGP Currency")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BalanceDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/balance")
    public BalanceDTO getBalance(@RequestHeader("Authorization") String token) {
        return accountService.getBalance(token);
    }

    @Operation(summary = "Get Account Balance using Account Number")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BalanceDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/balance")
    public BalanceDTO getAccountBalance(@RequestHeader("Authorization") String token, @RequestBody AccountNumberDTO acc)
            throws AccountNotFoundException {
        return accountService.getBalanceUsingAccountNumber(token, acc.getAccountNumber());
    }
}
