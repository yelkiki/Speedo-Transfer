package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.transactionDTOs.SendMoneyWithAccNumberDTO;
import com.example.speedotansfer.dto.transactionDTOs.TransferResponseDTO;
import com.example.speedotansfer.exception.custom.AccountNotFoundException;
import com.example.speedotansfer.exception.custom.InsufficientAmountException;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.exception.response.ErrorDetails;
import com.example.speedotansfer.service.impl.TransferService;
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
@RequestMapping("/api/transfer")
@Validated
public class TransferController {

    private final TransferService transferService;

    @Operation(summary = "Transfer Money using Account Number")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TransferResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description = "User Not Found", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthenticated", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Insufficient Amount", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping
    public TransferResponseDTO transferUsingAccountNumber
            (@RequestHeader("Authorization") String token, @RequestBody @Valid SendMoneyWithAccNumberDTO details)
            throws UserNotFoundException, InsufficientAmountException, AccountNotFoundException {
        return transferService.transferUsingAccNumber(token, details);

    }
}
