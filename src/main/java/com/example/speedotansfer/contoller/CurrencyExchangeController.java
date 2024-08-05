package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.transactionDTOs.GetExchangeRateDTO;
import com.example.speedotansfer.enums.Currency;
import com.example.speedotansfer.service.impl.CurrencyExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencyExchange")
@Validated
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @Operation(summary = "Get Exchange Rate")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GetExchangeRateDTO.class), mediaType = "application/json")})
    @GetMapping("/convert/{from}/{to}")
    public GetExchangeRateDTO getExchangeRate(@PathVariable Currency from, @PathVariable Currency to) {
        return new GetExchangeRateDTO(currencyExchangeService.getExchangeRate(from, to));
    }
}
