package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.transactionDTOs.AllTransactionsDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.exception.response.ErrorDetails;
import com.example.speedotansfer.service.impl.TransactionService;
import com.example.speedotansfer.service.impl.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
@Validated
public class TransactionController {
    private final TransactionService transactionService;
    private final TransferService transferService;


    @Operation(summary = "Get All Transactions")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AllTransactionsDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description = "User Not Found", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthenticated", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public AllTransactionsDTO getTransactions(@RequestHeader("Authorization") String token)
            throws UserNotFoundException {
        return transactionService.getHistory(token);
    }


}
