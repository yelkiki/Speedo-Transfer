package com.example.speedotansfer.contoller;

import com.example.speedotansfer.dto.userDTOs.AccountDTO;
import com.example.speedotansfer.exception.custom.UserNotFoundException;
import com.example.speedotansfer.service.impl.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping()
    public AccountDTO addCard(@RequestHeader("Authorization") String token, @RequestBody AccountDTO acc) throws UserNotFoundException {
        return accountService.addAccount(token, acc);
    }
}
